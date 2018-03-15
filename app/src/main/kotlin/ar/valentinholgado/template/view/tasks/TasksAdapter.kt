package ar.valentinholgado.template.view.tasks

import android.view.ViewGroup
import ar.valentinholgado.template.BR
import ar.valentinholgado.template.R
import ar.valentinholgado.template.view.common.CardAdapter
import ar.valentinholgado.template.view.common.CardEvent
import ar.valentinholgado.template.view.common.CardViewHolder

class TasksAdapter : CardAdapter<TaskUiModel>() {

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.viewholder_task
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CardViewHolder<TaskUiModel> {
        val viewholder = super.onCreateViewHolder(parent, viewType)

        viewholder.binding.setVariable(BR.checkboxHandler, object : CheckboxHandler {
            override fun onCheckboxChange(checked: Boolean, uiModel: TaskUiModel) {
                eventStream.onNext(CheckboxEvent(checked, uiModel))
            }
        })

        return viewholder
    }
}

data class CheckboxEvent(val checked: Boolean, val uiModel: TaskUiModel) : CardEvent<TaskUiModel>(uiModel)

interface CheckboxHandler {
    fun onCheckboxChange(checked: Boolean, uiModel: TaskUiModel)
}
