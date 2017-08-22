package ar.valentinholgado.template.backend.authentication

import ar.valentinholgado.template.backend.artsy.ArtsyApi
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.reactivex.Observable
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Field
import java.io.IOException
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded

object ServiceGenerator {

    val API_BASE_URL = "https://api.artsy.net/api/"

    private val httpClientBuilder = OkHttpClient.Builder()

    lateinit var artsyService: ArtsyApi

    private val retrofitBuilder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerKotlinModule()))

    fun <S> createLoginService(serviceClass: Class<S>): S {
        val retrofit = retrofitBuilder.client(httpClientBuilder.build()).build()
        return retrofit.create(serviceClass)
    }

    fun <S> createAuthenticatedService(serviceClass: Class<S>, authToken: String): S {
        val interceptor = AuthenticationInterceptor(authToken)

        if (interceptor !in httpClientBuilder.interceptors())
            httpClientBuilder.addInterceptor(interceptor)

        val httpClient = httpClientBuilder.build()
        val retrofit = retrofitBuilder.client(httpClient).build()

        return retrofit.create(serviceClass)
    }

    fun initializeAuthenticatedService(accessToken: String) {
        artsyService = createAuthenticatedService(ArtsyApi::class.java, accessToken)
    }
}



class AuthenticationInterceptor(private val authToken: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
                .header("X-Xapp-Token", authToken)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

interface LoginService {
    @FormUrlEncoded
    @POST("tokens/xapp_token")
    fun getAccessToken(@Field("client_id") clientId: String,
                       @Field("client_secret") clientSecret: String): Observable<AccessToken>
}

data class AccessToken(@JsonProperty("type") var type: String,
                       @JsonProperty("token") var token: String,
                       @JsonProperty("expires_at") var expiresAt: String? = null)