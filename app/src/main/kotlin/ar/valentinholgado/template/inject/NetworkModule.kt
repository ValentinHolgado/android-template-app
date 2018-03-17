package ar.valentinholgado.template.inject

import android.app.Application
import ar.valentinholgado.template.backend.Repository
import ar.valentinholgado.template.backend.StreamCache
import ar.valentinholgado.template.backend.artsy.ArtsyApi
import ar.valentinholgado.template.backend.artsy.ArtsyRepository
import ar.valentinholgado.template.backend.audio.AudioRepository
import ar.valentinholgado.template.backend.files.FilesRepository
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.piasy.rxandroidaudio.AudioRecorder
import com.github.piasy.rxandroidaudio.RxAudioPlayer
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(cache: Cache, @Named("api_token") apiToken: String): OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .addNetworkInterceptor { chain: Interceptor.Chain? ->
                    chain?.proceed(chain.request()
                            .newBuilder()
                            .addHeader("X-Xapp-Token", apiToken)
                            .build())
                }
                .cache(cache)
                .build()
    }

    @Provides
    fun provideObjectMapper(): ObjectMapper {
        return ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerKotlinModule()
    }

    @Provides
    @ApplicationScope
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient,
                               objectMapper: ObjectMapper): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
    }

    @Provides
    @ApplicationScope
    fun provideRetrofit(builder: Retrofit.Builder,
                        @Named("base_url") baseUrl: String): Retrofit {
        return builder
                .baseUrl(baseUrl)
                .build()
    }

    @Provides
    fun provideObservablesLruCache(): StreamCache {
        return StreamCache()
    }

    @Provides
    @ApplicationScope
    fun provideArtsyApi(retrofit: Retrofit): ArtsyApi {
        return retrofit.create(ArtsyApi::class.java)
    }

    @Provides
    @ApplicationScope
    fun provideArtsyRepository(artsyApi: ArtsyApi,
                               cache: StreamCache): ArtsyRepository {
        return ArtsyRepository(artsyApi, cache)
    }

    @Provides
    @ApplicationScope
    fun provideRxAudioPlayer(): RxAudioPlayer {
        return RxAudioPlayer.getInstance()
    }

    @Provides
    @ApplicationScope
    fun provideAudioRecorder(): AudioRecorder {
        return AudioRecorder.getInstance()
    }

    @Provides
    @ApplicationScope
    fun provideAudioRepository(rxAudioPlayer: RxAudioPlayer, audioRecorder: AudioRecorder): AudioRepository {
        return AudioRepository(rxAudioPlayer = rxAudioPlayer, audioRecorder = audioRecorder)
    }

    @Provides
    @ApplicationScope
    fun provideFilesRepository(application: Application): FilesRepository {
        return FilesRepository(context = application)
    }

    @Provides
    @ApplicationScope
    fun provideRepository(artsyRepository: ArtsyRepository, audioRepository: AudioRepository): Repository {
        return Repository(artsyRepository = artsyRepository, audioRepository = audioRepository)
    }
}
