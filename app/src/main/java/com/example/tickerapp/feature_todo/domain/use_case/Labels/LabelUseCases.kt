package com.example.tickerapp.feature_todo.domain.use_case.Labels

data class LabelUseCases(
    val addLabel: AddLabel,
    val getLabels: GetLabels,
    val deleteLabel: DeleteLabel,
    val updateLabel: UpdateLabel
)