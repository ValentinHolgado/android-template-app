package ar.valentinholgado.template.view.feed

import android.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView

/**
 * Home view state
 */
data class FeedUiModel(val screenTitle: String = "",
                       val contentList: List<CardContent>? = null,
                       val errorMessage: String? = null,
                       val showInputText: Boolean = true,
                       val isLoading: Boolean = false)

data class CardContent(val title: String,
                       val subtitle: String? = "",
                       val imageUri: String? = "",
                       val intentUri: String? = null) {

    companion object {

        @JvmStatic
        @BindingAdapter("app:imageUrl")
        fun loadImage(view: SimpleDraweeView, imageUrl: String?) {
            view.setImageURI(imageUrl)
        }
    }
}

