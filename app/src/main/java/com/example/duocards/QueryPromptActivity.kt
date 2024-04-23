package com.example.duocards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.example.duocards.R

class QueryPromptActivity : AppCompatActivity() {
    private lateinit var editTextQuery: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_prompt)

        editTextQuery = findViewById(R.id.editTextQuery)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val query = editTextQuery.text.toString()
            // Pass the query back to the calling activity
            val resultIntent = Intent()
            resultIntent.putExtra("QUERY_RESULT", query)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }
}
