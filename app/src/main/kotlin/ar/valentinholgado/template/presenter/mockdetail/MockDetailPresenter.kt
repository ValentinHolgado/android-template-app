package ar.valentinholgado.template.presenter.mockdetail

import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.detail.DetailContent
import ar.valentinholgado.template.view.detail.DetailEvent
import ar.valentinholgado.template.view.detail.DetailUiModel

class MockDetailPresenter constructor(detailView: ReactiveView<DetailUiModel, DetailEvent>) {

    init {

        val uiModel = DetailUiModel(
                content = DetailContent(
                        title = "Mocked detail title",
                        subtitle = "Mocked detail subtitle",
                        description = "Lorem ipsum dolor sit amet, ut nec semper maluisset. Ornatus fuisset nam id. Eos vero tollit in. Mea recusabo omittantur comprehensam cu, pro oblique docendi in. Ad cum labore molestiae, wisi liber senserit an mel.\n" +
                                "\n" +
                                "Ei doming intellegat mei, an porro luptatum per. Ad hinc tollit voluptatum quo, no cum agam debitis torquatos. Vel diceret corrumpit vituperata ut, est ea omnes noster. Per no ullum mucius saperet, eos reque commune pertinacia in.\n" +
                                "\n" +
                                "Quas ignota vituperatoribus te nec, at sed suscipit adversarium definitiones, no vix nibh nominavi. Sit ad debet meliore consequuntur, mea et causae vocent. Ipsum noster eu mea, facilis eloquentiam et est. Id altera corpora usu, vim ne posse feugiat, expetenda intellegebat sit no. Ius laudem cetero efficiantur ad. Ex movet invidunt repudiandae pro.\n" +
                                "\n" +
                                "Eam clita nonumy ei, eum alii cetero suscipiantur ex. Cu quo ubique vulputate. Eu noster dictas assueverit pro, ius doming malorum perfecto id. Ea purto wisi nemore qui.\n" +
                                "\n" +
                                "Alterum delicata scripserit an pro. Ea facete instructior his, malorum principes splendide at vel. Erat discere vim ad, no utroque appetere eam. Audiam appetere mei no, no sea mutat nullam, sea ferri postea deleniti cu. Eu lorem suavitate intellegebat sea, primis latine lucilius pro an. Ut vel exerci regione nominavi, vivendo pertinax no est.",
                        imageUri = "https://images.duckduckgo.com/iu/?u=http%3A%2F%2Fweknowmemes.com%2Fwp-content%2Fuploads%2F2013%2F11%2Fdoge-original-meme.jpg&f=1"))

        detailView.inputStream().onNext(uiModel)
    }
}