package org.tourlink.routingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tourlink.routingservice.dto.PathPlanRequest;
import org.tourlink.routingservice.dto.PathPlanResponse;
import org.tourlink.routingservice.entity.*;
import org.tourlink.routingservice.respository.SpotRepository;
import org.tourlink.routingservice.respository.TransportStationRepository;
import org.tourlink.routingservice.utils.GeoUtils;

import java.util.*;

@Service
public class PathPlanningServiceImpl implements PathPlanningService {
    @Autowired
    private TransportStationRepository stationRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Override
    public PathPlanResponse planRoute(PathPlanRequest request) {
        List<Spot> allSpots = spotRepository.findAll();
        String transportMode = request.getTransportMode();

        // 调用遗传算法核心逻辑（假设已封装）
        PathPlanResponse response = new PathPlanResponse();

        // 判断交通方式是否需要自动选择
        if ((!"TRAIN".equalsIgnoreCase(transportMode) && !"AIRPORT".equalsIgnoreCase(transportMode))) {
            List<TransportStation> originAirports = stationRepository.findByCityNameAndType(request.getFromCity(), "AIRPORT");
            List<TransportStation> originTrains = stationRepository.findByCityNameAndType(request.getFromCity(), "TRAIN");

            List<TransportStation> destAirports = stationRepository.findByCityNameAndType(request.getToCity(), "AIRPORT");
            List<TransportStation> destTrains = stationRepository.findByCityNameAndType(request.getToCity(), "TRAIN");

            double minFlightDistance = Double.MAX_VALUE;
            double minTrainDistance = Double.MAX_VALUE;
            TransportStation bestFromAirport = null, bestToAirport = null;
            TransportStation bestFromTrain = null, bestToTrain = null;

            for (TransportStation o : originAirports) {
                for (TransportStation d : destAirports) {
                    double dist = GeoUtils.calculateDistance(o.getLatitude(), o.getLongitude(), d.getLatitude(), d.getLongitude());
                    if (dist < minFlightDistance) {
                        minFlightDistance = dist;
                        bestFromAirport = o;
                        bestToAirport = d;
                    }
                }
            }

            for (TransportStation o : originTrains) {
                for (TransportStation d : destTrains) {
                    double dist = GeoUtils.calculateDistance(o.getLatitude(), o.getLongitude(), d.getLatitude(), d.getLongitude());
                    if (dist < minTrainDistance) {
                        minTrainDistance = dist;
                        bestFromTrain = o;
                        bestToTrain = d;
                    }
                }
            }

            // 选择最终的交通方式
            double chosenDistance;
            TransportStation originStation;
            TransportStation destStation;

            if (minTrainDistance <= 1000 || minFlightDistance == Double.MAX_VALUE) {
                transportMode = "TRAIN";
                chosenDistance = minTrainDistance;
                originStation = bestFromTrain;
                destStation = bestToTrain;
            } else {
                transportMode = "AIRPORT";
                chosenDistance = minFlightDistance;
                originStation = bestFromAirport;
                destStation = bestToAirport;
            }

            double estimatedPrice = GeoUtils.estimatePrice(chosenDistance, transportMode);
            System.out.println("推荐交通方式: " + transportMode + "，预计距离: " + chosenDistance + " km，估算价格: ¥" + estimatedPrice);

            // 构造估计对象
            if (originStation != null && destStation != null) {
                TransportEstimate estimate = new TransportEstimate();
                estimate.setFromStation(originStation.getName());
                estimate.setToStation(destStation.getName());
                estimate.setTransportType(transportMode);
                estimate.setEstimatedDistance(chosenDistance);
                estimate.setEstimatedPrice(estimatedPrice);
                response.setTransportEstimate(estimate);
            }
        }

        // 归一化偏好
        UserPreference pref = normalizePreference(request.getUserPreference());
        // 初始化种群
        List<List<Spot>> population = initPopulation(allSpots);
        List<Spot> best = null;
        double bestFitness = -1;

        for (int gen = 0; gen < 100; gen++) {
            // 评估适应度
            population.sort((a, b) -> Double.compare(fitness(b, pref), fitness(a, pref)));

            // 记录最优个体
            double f = fitness(population.get(0), pref);
            if (f > bestFitness) {
                bestFitness = f;
                best = new ArrayList<>(population.get(0));
            }

            // 选择 + 交叉 + 变异
            List<List<Spot>> nextGen = new ArrayList<>();
            for (int i = 0; i < 10; i++) nextGen.add(new ArrayList<>(population.get(i))); // 保留前10
            while (nextGen.size() < 50) {
                List<Spot> parent1 = select(population);
                List<Spot> parent2 = select(population);
                List<Spot> child = crossover(parent1, parent2);
                mutate(child);
                nextGen.add(child);
            }
            population = nextGen;
        }

        // 构造响应
        if (best == null) {
            best = population.get(0); // 或随机一条
        }
        List<PlannedRoute> routes = decode(best);
        response.setDailyRoutes(routes);
//        response.setEstimatedTransportPrice(transportPrice);
        return response;
    }


    private UserPreference normalizePreference(UserPreference pref) {
        Map<String, Double> norm = new HashMap<>();
        double sum = pref.getTagWeights().values().stream().mapToDouble(Double::doubleValue).sum();
        for (Map.Entry<String, Double> e : pref.getTagWeights().entrySet()) {
            norm.put(e.getKey(), e.getValue() / sum);
        }
        pref.setTagWeights(norm);
        return pref;
    }

    private List<List<Spot>> initPopulation(List<Spot> spots) {
        int size = 50;
        List<List<Spot>> pop = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<Spot> shuffled = new ArrayList<>(spots);
            Collections.shuffle(shuffled);
            pop.add(shuffled);
        }
        return pop;
    }

    private List<Spot> select(List<List<Spot>> population) {
        return population.get(new Random().nextInt(10)); // 择优随机选前10
    }

    private List<Spot> crossover(List<Spot> p1, List<Spot> p2) {
        int n = p1.size();
        int cut = new Random().nextInt(n);
        Set<Long> ids = new HashSet<>();
        List<Spot> child = new ArrayList<>();
        for (int i = 0; i < cut; i++) {
            child.add(p1.get(i));
            ids.add(p1.get(i).getId());
        }
        for (Spot s : p2) {
            if (!ids.contains(s.getId())) child.add(s);
        }
        return child;
    }

    private void mutate(List<Spot> list) {
        Random r = new Random();
        int i = r.nextInt(list.size());
        int j = r.nextInt(list.size());
        Collections.swap(list, i, j);
    }


    private double fitness(List<Spot> sequence, UserPreference pref) {
        List<PlannedRoute> plan = decode(sequence);
        double score = 0;
        for (PlannedRoute route : plan) {
            int timeUtil = 0;
            for (PlannedSpot ps : route.getSpots()) {
                Spot spot = ps.getSpot();
                double rating = spot.getRating() > 0 ? spot.getRating() : 3.5;
                score += rating * 2;
                for (String tag : spot.getTags()) {
                    score += pref.getTagWeights().getOrDefault(tag, 0.0);
                    if (pref.getSelectedTags().contains(tag)) score += 2.0;
                }
                score -= 0.02 * spot.getPrice();
                score -= 0.01 * spot.getSales();

                if (ps.getAssignedTimeSlot().contains("上午")) timeUtil |= 1;
                if (ps.getAssignedTimeSlot().contains("下午")) timeUtil |= 2;
                if (ps.getAssignedTimeSlot().contains("晚上")) timeUtil |= 4;
            }

            if (timeUtil == 7) score += 5;
            else if (timeUtil == 3 || timeUtil == 6 || timeUtil == 5) score += 3;
            else if (timeUtil > 0) score += 1;
        }
        return score;
    }
    private List<List<Spot>> splitIntoDays(List<Spot> route, int maxDays) {
        List<List<Spot>> result = new ArrayList<>();
        int total = route.size();
        int perDay = (int) Math.ceil((double) total / maxDays);

        for (int i = 0; i < maxDays; i++) {
            int start = i * perDay;
            int end = Math.min(start + perDay, total);
            if (start < end) {
                result.add(route.subList(start, end));
            }
        }

        return result;
    }

    private List<PlannedRoute> decode(List<Spot> route) {
        // 显式指定每天最多容纳的时间段（上午100、下午010、晚上001 => 3 bit）
        final int MAX_DAYS = 3; // 可按需扩展

        // 每天的景点列表
        List<List<Spot>> dailySpots = splitIntoDays(route, MAX_DAYS); // 拆分为每天的子列表

        // 可读时间段映射
        Map<String, String> readable = Map.of(
                "100", "上午",
                "010", "下午",
                "001", "晚上",
                "110", "上午+下午",
                "011", "下午+晚上",
                "101", "上午+晚上",
                "111", "全天"
        );

        // 编码对应的位掩码
        Map<String, Integer> timeMap = Map.of(
                "100", 0b100,
                "010", 0b010,
                "001", 0b001,
                "110", 0b110,
                "011", 0b011,
                "101", 0b101,
                "111", 0b111
        );

        List<PlannedRoute> result = new ArrayList<>();

        for (int dayIndex = 0; dayIndex < dailySpots.size(); dayIndex++) {
            List<Spot> spots = dailySpots.get(dayIndex);
            List<PlannedSpot> currentDay = new ArrayList<>();
            int timeMask = 0;

            for (Spot spot : spots) {
                boolean assigned = false;
                for (String slot : spot.getTimeSlots()) {
                    int mask = timeMap.getOrDefault(slot, 0);
                    if ((mask & timeMask) == 0) {
                        // 此时间段可用
                        timeMask |= mask;

                        PlannedSpot ps = new PlannedSpot();
                        ps.setSpot(spot);
                        ps.setAssignedTimeSlot(readable.getOrDefault(slot, "未知"));
                        currentDay.add(ps);

                        assigned = true;
                        break;
                    }
                }
                if (!assigned) {
                    System.out.println("未能安排景点：" + spot.getName() + "（时间段冲突）");
                }
            }

            PlannedRoute routeForDay = new PlannedRoute();
            routeForDay.setDay(dayIndex + 1);
            routeForDay.setSpots(currentDay);
            result.add(routeForDay);
        }

        return result;
    }
}
