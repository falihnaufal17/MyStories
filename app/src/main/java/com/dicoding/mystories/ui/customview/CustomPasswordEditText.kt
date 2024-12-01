package com.dicoding.mystories.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.mystories.R

class CustomPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var isPasswordVisible: Boolean = false
    private var passwordIcon: Drawable
    private var lockIcon: Drawable

    init {
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_off_24) as Drawable
        lockIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable

        setButtonDrawables(endOfTheText = passwordIcon, startOfTheText = lockIcon)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkPasswordLength()
            }
        })

        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val drawableEnd = compoundDrawables[2]
            if (drawableEnd != null) {
                val iconStartX = width - paddingEnd - drawableEnd.intrinsicWidth

                if (event.x >= iconStartX) {
                    togglePasswordVisibility()
                    return true
                }
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        val currentTypeface = typeface
        inputType = if (isPasswordVisible) {
            passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_24) as Drawable
            setButtonDrawables(endOfTheText = passwordIcon)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            passwordIcon = ContextCompat.getDrawable(context,
                R.drawable.ic_baseline_visibility_off_24
            ) as Drawable
            setButtonDrawables(endOfTheText = passwordIcon)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        typeface = currentTypeface
        setSelection(text?.length ?: 0)
    }

    private fun checkPasswordLength() {
        if ((text?.length ?: 0) < 8) {
            setError(resources.getString(R.string.err_password_length), null)
        } else {
            error = null
        }
    }
}