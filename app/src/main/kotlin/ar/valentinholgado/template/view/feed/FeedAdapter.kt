package ar.valentinholgado.template.view.feed

import ar.valentinholgado.template.R
import ar.valentinholgado.template.view.common.CardAdapter

class FeedAdapter : CardAdapter<CardContent>() {

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.viewholder_home
    }
}