package nl.gamma.shoppingcart

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@EnableTransactionManagement
class ShoppingcartApplication {

	@Bean
	fun productsGroup(): GroupedOpenApi {
		return GroupedOpenApi.builder().group("Products")
			.addOperationCustomizer { operation, handlerMethod ->
				operation.addSecurityItem(SecurityRequirement().addList("basicScheme"))
				operation
			}
			.addOpenApiCustomizer { openApi -> openApi.info(Info().title("Products API")) }
			.packagesToScan("nl.gamma.shoppingcart")
			.build()
	}
}

fun main(args: Array<String>) {
	runApplication<ShoppingcartApplication>(*args)
}
