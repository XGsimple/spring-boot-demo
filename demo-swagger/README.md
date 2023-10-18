# spring-boot-demo-swagger

> 此 demo 主要演示了 Spring Boot 如何集成原生 swagger ，自动生成 API 文档。
>
> 启动项目，访问地址：http://localhost:8080/demo/swagger-ui.html#/

# Swagger 注解

## 1.@Api

用在请求的类上，表示对类的说明，也代表了这个类是swagger2的资源。

````
参数：

tags：说明该类的作用，参数是个数组，可以填多个。

value：“该参数没什么意义，在UI界面上不显示，所以不用配置”

description：“用户基本信息操作”

````

## 2.@ApiOperation()

````
用于方法，表示一个http请求访问该方法的操作。

参数：

value：“方法的用途和作用”

notes：“方法的注意事项和备注”

tags：说明该方法的作用，参数是个数组，可以填多个。格式：tags={“作用1”,“作用2”}

（在这里建议不使用这个参数，会使界面看上去有点乱，前两个常用）
````

## 3.@ApiModel()

````
用于实体类上，用于说明实体作用。

参数：

description：“描述实体的作用”
````

## 4.@ApiModelProperty

用在属性上，描述实体类的属性

````
参数：

value：描述参数的意义

name：参数的变量名

required：参数是否必选
````

## 5. @ApiImplicitParams

用在请求的方法上，包含多@ApiImplicitParam

## 6. @ApiImplicitParam

用于方法，表示单独的请求参数。

````

参数：

name：“参数名”

value：“参数说明”

dataType：“数据类型”

paramType： 表示参数放在哪里

defaultValue：“参数的默认值”

required：表示参数是否必须传
````

## 7. @ApiParam()

用于方法，参数，字段说明 表示对参数的要求和说明。

````

参数：

name：“参数名称”

value：“参数的简要说明”

defaultValue：“参数默认值”

required：表示属性是否必填，默认为false
````

## 8. @ApiResponses

用于请求的方法上，根据响应码表示不同响应。 一个@ApiResponses包含多个@ApiResponse。

## 9. @ApiResponse

用在请求的方法上，表示不同的响应。

## 10. @ApiIgnore()

用于类或者方法上，不被显示在页面上。

## 11. @Profile({“dev”, “test”})

用于配置类上，表示只对开发和测试环境有用。

# pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-boot-demo-swagger</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>spring-boot-demo-swagger</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.xkcoding</groupId>
        <artifactId>spring-boot-demo</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <swagger.version>2.9.2</swagger.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>${swagger.version}</version>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>${swagger.version}</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <finalName>spring-boot-demo-swagger</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

## Swagger2Config.java

```java
/**
 * <p>
 * Swagger2 配置
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-29 11:14
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                                                      .select()
                                                      .apis(RequestHandlerSelectors.basePackage(
                                                          "com.xkcoding.swagger.controller"))
                                                      .paths(PathSelectors.any())
                                                      .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("spring-boot-demo")
                                   .description("这是一个简单的 Swagger API 演示")
                                   .contact(new Contact("Yangkai.Shen", "http://xkcoding.com", "237497819@qq.com"))
                                   .version("1.0.0-SNAPSHOT")
                                   .build();
    }

}
```

## UserController.java

> 主要演示API层的注解。

```java
/**
 * <p>
 * User Controller
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-29 11:30
 */
@RestController
@RequestMapping("/user")
@Api(tags = "1.0.0-SNAPSHOT", description = "用户管理", value = "用户管理")
@Slf4j
public class UserController {
    @GetMapping
    @ApiOperation(value = "条件查询（DONE）", notes = "备注")
    @ApiImplicitParams({@ApiImplicitParam(name = "username",
                                          value = "用户名",
                                          dataType = DataType.STRING,
                                          paramType = ParamType.QUERY,
                                          defaultValue = "xxx")})
    public ApiResponse<User> getByUserName(String username) {
        log.info("多个参数用  @ApiImplicitParams");
        return ApiResponse.<User>builder().code(200).message("操作成功").data(new User(1, username, "JAVA")).build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "主键查询（DONE）", notes = "备注")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",
                                          value = "用户编号",
                                          dataType = DataType.INT,
                                          paramType = ParamType.PATH)})
    public ApiResponse<User> get(@PathVariable Integer id) {
        log.info("单个参数用  @ApiImplicitParam");
        return ApiResponse.<User>builder().code(200).message("操作成功").data(new User(id, "u1", "p1")).build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户（DONE）", notes = "备注")
    @ApiImplicitParam(name = "id", value = "用户编号", dataType = DataType.INT, paramType = ParamType.PATH)
    public void delete(@PathVariable Integer id) {
        log.info("单个参数用 ApiImplicitParam");
    }

    @PostMapping
    @ApiOperation(value = "添加用户（DONE）")
    public User post(@RequestBody User user) {
        log.info("如果是 POST PUT 这种带 @RequestBody 的可以不用写 @ApiImplicitParam");
        return user;
    }

    @PostMapping("/multipar")
    @ApiOperation(value = "添加用户（DONE）")
    public List<User> multipar(@RequestBody List<User> user) {
        log.info("如果是 POST PUT 这种带 @RequestBody 的可以不用写 @ApiImplicitParam");

        return user;
    }

    @PostMapping("/array")
    @ApiOperation(value = "添加用户（DONE）")
    public User[] array(@RequestBody User[] user) {
        log.info("如果是 POST PUT 这种带 @RequestBody 的可以不用写 @ApiImplicitParam");
        return user;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改用户（DONE）")
    public void put(@PathVariable Long id, @RequestBody User user) {
        log.info("如果你不想写 @ApiImplicitParam 那么 swagger 也会使用默认的参数名作为描述信息 ");
    }

    @PostMapping("/{id}/file")
    @ApiOperation(value = "文件上传（DONE）")
    public String file(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        log.info(file.getContentType());
        log.info(file.getName());
        log.info(file.getOriginalFilename());
        return file.getOriginalFilename();
    }
}
```

## ApiResponse.java

> 主要演示了 实体类 的注解。

```java
/**
 * <p>
 * 通用API接口返回
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-29 11:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "通用PI接口返回", description = "Common Api Response")
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = -8987146499044811408L;
    /**
     * 通用返回状态
     */
    @ApiModelProperty(value = "通用返回状态", required = true)
    private Integer code;
    /**
     * 通用返回信息
     */
    @ApiModelProperty(value = "通用返回信息", required = true)
    private String message;
    /**
     * 通用返回数据
     */
    @ApiModelProperty(value = "通用返回数据", required = true)
    private T data;
}
```

## 参考

1. swagger 官方网站：https://swagger.io/

2. swagger 官方文档：https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started

3. swagger 常用注解：https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations
