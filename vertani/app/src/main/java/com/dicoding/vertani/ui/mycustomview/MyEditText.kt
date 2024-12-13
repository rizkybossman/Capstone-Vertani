package com.dicoding.vertani.ui.mycustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.vertani.R


class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var clearButtonImage: Drawable
    private var isEmailInput: Boolean = false

    init {

        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as Drawable


        setOnTouchListener(this)


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInput(s.toString())
                if (isEmailInput && s.toString().isNotEmpty()) {
                    showClearButton()
                } else {
                    hideClearButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }


    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }


    private fun hideClearButton() {
        setButtonDrawables()
    }


    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null && isEmailInput) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                if (event.x < clearButtonEnd) isClearButtonClicked = true
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                if (event.x > clearButtonStart) isClearButtonClicked = true
            }

            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as Drawable
                        text?.clear()
                        hideClearButton()
                        return true
                    }
                }
            }
        }
        return false
    }


    private fun validateInput(input: String) {
        if (isEmailInput) {
            if (input.isEmpty()) {
                error = "Email tidak boleh kosong"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                error = "Format email tidak valid"
            } else {
                error = null
            }
        } else {
            if (input.length < 8) {
                error = "Password tidak boleh kurang dari 8 karakter"
            } else {
                error = null
            }
        }
    }


    fun setEmailInput(isEmail: Boolean) {
        isEmailInput = isEmail
        invalidate()
    }
}
