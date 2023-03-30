import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.google.gson.JsonObject
import com.google.gson.JsonParser


class Application : RequestHandler<LambdaProxyEvent, LambdaProxyOutput> {
    override fun handleRequest(event: LambdaProxyEvent, context: Context): LambdaProxyOutput {
        println("Event: $event \nContext $context")
        val path = event.path
        val method = event.httpMethod
        val headers: HashMap<String, String> = hashMapOf()
        headers.put("Access-Control-Allow-Origin", "*")
        headers.put("Access-Control-Allow-Headers" , "Content-Type")
        if (method == "POST") {
            val body = event.body ?: ""
            if (body.isNotEmpty()) {
                val originalUrl = body.toJson().get("originalUrl").asString
                val resJson = JsonObject()
                resJson.addProperty("id", "randomId")
                resJson.addProperty("originalUrl", originalUrl)
                resJson.addProperty("shortenUrl", "https://ini-shorting")
                resJson.addProperty("title", "hasil scraping")
                return LambdaProxyOutput(headers = headers, body = resJson.toString())
            } else return LambdaProxyOutput(headers = headers, body = "Hello Kotlin")
        } else if (method == "GET") {
            val array = arrayListOf<JsonObject>()
            for (i in 1..3) {
                val resJson = JsonObject()
                resJson.addProperty("id", "randomId"+i)
                resJson.addProperty("originalUrl", "https://url-sample")
                resJson.addProperty("title", "sample tile "+i)
                array.add(resJson)
            }
            return LambdaProxyOutput(headers = headers, body = array.toString())
        } else if (method == "DELETE") {
            return LambdaProxyOutput(headers = headers, body = "true")
        } else {
            return LambdaProxyOutput(headers = headers, body = "Hello Kotlin")
        }
    }
}

fun String.toJson() = JsonParser.parseString(this).asJsonObject

data class LambdaProxyOutput(
    var statusCode: Int = 200,
    var headers: Map<String, String> = hashMapOf(),
    var body: String = ""
)

data class LambdaProxyEvent(
    var resource: String? = null,
    var path: String? = null,
    var httpMethod: String? = null,
    var headers: Map<String,String>? = null,
    var queryStringParameters: Map<String,String>? = null,
    var pathParameters: Map<String,String>? = null,
    var stageVariables: Map<String,String>? = null,
    var requestContext: RequestContext? = null,
    var body: String? = null,
    var isBase64Encoded: Boolean = false
) {
    data class RequestContext(
        var accountId: String? = null,
        var resourceId: String? = null,
        var stage: String? = null,
        var requestId: String? = null,
        var identity: Identity? = null,
        var resourcePath: String? = null,
        var httpMethod: String? = null,
        var apiId: String? = null
    )

    data class Identity(
        var cognitoIdentityPoolId: String? = null,
        var accountId: String? = null,
        var cognitoIdentityId: String? = null,
        var caller: String? = null,
        var apiKey: String? = null,
        var sourceIp: String? = null,
        var cognitoAuthenticationType: String? = null,
        var cognitoAuthenticationProvider: String? = null,
        var userArn: String? = null,
        var userAgent: String? = null,
        var user: String? = null
    )
}