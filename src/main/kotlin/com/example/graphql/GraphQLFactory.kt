package com.example.graphql

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Singleton

/**
 *
 */
@Factory
class GraphQLFactory {

    /**
     *
     */
    @Bean
    @Singleton
    fun graphQL(resourceResolver: ResourceResolver, helloDataFetcher: HelloDataFetcher): GraphQL {

        // Parse the schema.
        val typeRegistry = TypeDefinitionRegistry().apply {
            this.merge(
                SchemaParser().parse(
                    BufferedReader(
                        InputStreamReader(
                            resourceResolver.getResourceAsStream("classpath:schema.graphqls").get()
                        )
                    )
                )
            )
        }

        // Create the runtime wiring.
        val runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query") { typeWiring ->
                typeWiring
                    .dataFetcher("hello", helloDataFetcher)
            }
            .build()

        // Create the executable schema.
        val graphQLSchema = SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)

        // Return the GraphQL bean.
        return GraphQL.newGraphQL(graphQLSchema).build()
    }
}
