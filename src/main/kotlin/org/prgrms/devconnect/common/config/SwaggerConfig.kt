package org.prgrms.devconnect.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig() {

  @Bean
  fun customOpenAPI(): OpenAPI {
    return OpenAPI()
        .info(Info()
            .title("springdoc-openapi")
            .version("1.0")
            .description("DevConnector swagger-ui 화면입니다.")
        )
        .components(Components()
            .addSecuritySchemes("AccessToken",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
        .addSecurityItem(SecurityRequirement().addList("AccessToken"))
  }
}