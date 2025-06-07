# 用户服务 (User Service)

用户服务负责管理用户信息，包括用户注册、登录、个人信息管理等功能。

## 微服务间调用

### 获取用户基本信息 (UserBasicDTO)

其他服务可以通过Feign客户端调用用户服务的API来获取用户基本信息。

#### 1. 在你的服务中添加Feign客户端接口

```java
package org.tourlink.yourservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.tourlink.common.dto.userDTO.UserBasicDTO;
import org.tourlink.common.response.ApiResponse;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {

    /**
     * 获取用户基本信息
     * @param id 用户ID
     * @return 用户基本信息
     */
    @GetMapping("/api/users/{id}/basic")
    ApiResponse<UserBasicDTO> getUserBasicInfo(@PathVariable("id") Long id);

    /**
     * 批量获取多个用户的基本信息
     * @param ids 用户ID列表，以逗号分隔
     * @return 多个用户的基本信息列表
     */
    @GetMapping("/api/users/batch/basic")
    ApiResponse<List<UserBasicDTO>> getBatchUserBasicInfo(@RequestParam("ids") String ids);
}
```

#### 2. 确保你的服务启用了Feign客户端

在你的主应用类上添加`@EnableFeignClients`注解：

```java
@SpringBootApplication
@EnableFeignClients
public class YourServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourServiceApplication.class, args);
    }
}
```

#### 3. 在你的服务中注入并使用UserClient

```java
@Service
public class YourService {
    
    @Autowired
    private UserClient userClient;
    
    public UserBasicDTO getUserInfo(Long userId) {
        ApiResponse<UserBasicDTO> response = userClient.getUserBasicInfo(userId);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }
    
    public List<UserBasicDTO> getBatchUserInfo(List<Long> userIds) {
        String ids = userIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        
        ApiResponse<List<UserBasicDTO>> response = userClient.getBatchUserBasicInfo(ids);
        if (response.isSuccess()) {
            return response.getData();
        }
        return Collections.emptyList();
    }
}
```

## API 文档

### 获取单个用户基本信息

- **URL**: `/api/users/{id}/basic`
- **Method**: GET
- **Path Variable**: `id` - 用户ID
- **Response**: 
  ```json
  {
    "success": true,
    "code": "200",
    "message": "操作成功",
    "data": {
      "id": 1,
      "username": "user1",
      "email": "user1@example.com",
      "avatarUrl": "http://example.com/avatar.jpg"
    }
  }
  ```

### 批量获取多个用户基本信息

- **URL**: `/api/users/batch/basic`
- **Method**: GET
- **Query Parameter**: `ids` - 用户ID列表，以逗号分隔，例如 `1,2,3`
- **Response**: 
  ```json
  {
    "success": true,
    "code": "200",
    "message": "操作成功",
    "data": [
      {
        "id": 1,
        "username": "user1",
        "email": "user1@example.com",
        "avatarUrl": "http://example.com/avatar1.jpg"
      },
      {
        "id": 2,
        "username": "user2",
        "email": "user2@example.com",
        "avatarUrl": "http://example.com/avatar2.jpg"
      }
    ]
  }
  ```
