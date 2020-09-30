package com.android.testcoroutinehiltkola.model

import com.android.testcoroutinehiltkola.enums.ENUMEXPENSECATEGORYTYPE

data class ExpenseCategory(val
                           name: Int,
                           val image: Int,
                           val color: Int,
                           val type: ENUMEXPENSECATEGORYTYPE?,
                           val background: Int) {
    constructor():this(0, 0, 0, null, 0)
}