package ar.valentinholgado.template

import ar.valentinholgado.template.backend.artsy.Link
import ar.valentinholgado.template.backend.artsy.Links
import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.artsy.search.Entry
import ar.valentinholgado.template.backend.artsy.search.SearchResult
import ar.valentinholgado.template.presenter.home.HomePresenter
import ar.valentinholgado.template.view.feed.FeedUiModel
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(JUnitPlatform::class)
class SomeSpec : Spek({
    describe("a home presenter accumulator") {
        val accumulator = HomePresenter.accumulator

        on("a successful result") {
            val model = accumulator.invoke(FeedUiModel(), SearchResult(status = Result.Status.SUCCESS))

            it("should  not be loading") {
                assertFalse { model.isLoading }
            }
        }

        on("an error result") {
            val model = accumulator.invoke(FeedUiModel(), SearchResult(status = Result.Status.ERROR))

            it("should not be loading") {
                assertFalse { model.isLoading }
            }
        }

        on("an in flight result") {
            val model = accumulator.invoke(FeedUiModel(), SearchResult(status = Result.Status.IN_FLIGHT))

            it("should be loading") {
                assertTrue { model.isLoading }
            }
        }

        on("a successful search result") {
            val list = ArrayList<Entry>()
            list.add(Entry(
                    type = "artist",
                    title = "Xul Solar",
                    og_type = "artist",
                    links = Links(
                            thumbnail = Link(
                                    url = "http://some/url",
                                    templated = false),
                            self = Link(
                                    url = "api.artsy.com/some_id",
                                    templated = false)),
                    description = "Some description"))

            val model = accumulator.invoke(FeedUiModel(), SearchResult(status = Result.Status.SUCCESS,
                    body = list))

            it("should return model with result's content") {
                model.contentList!![0].let {
                    assertEquals("Xul Solar", it.title)
                    assertEquals("artist", it.subtitle)
                    assertEquals("app://detail?id=some_id&type=artist", it.intentUri)
                    assertEquals("http://some/url", it.imageUri)
                }
            }
        }
    }
})