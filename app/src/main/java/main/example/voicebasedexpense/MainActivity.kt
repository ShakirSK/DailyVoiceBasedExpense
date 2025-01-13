package main.example.voicebasedexpense

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import main.example.voicebasedexpense.data.model.Expense
import main.example.voicebasedexpense.presentation.view.adapter.ExpenseAdapter
import main.example.voicebasedexpense.presentation.viewmodel.ExpenseViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_SPEECH_INPUT = 100
    }

    private lateinit var btnVoiceInput: Button

    private lateinit var adapter: ExpenseAdapter

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVoiceInput = findViewById(R.id.button)

        btnVoiceInput.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                startSpeechRecognition()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            }
        }

        adapter = ExpenseAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        viewModel.allExpenses.observe(this, { expenses ->
            expenses?.let {
                adapter.submitList(it)
                // Update total amount
                val totalAmount = viewModel.calculateTotalAmount(it)
                val totalExpensesText = findViewById<TextView>(R.id.totalExpensesText)
                totalExpensesText.text = "₹${String.format("%.2f", totalAmount)}"
                updateProgressBar(totalAmount)
            }
        })
    }


    private fun updateProgressBar(total: Double) {
        // Assuming a max budget of ₹50,000 for progress calculation
        val maxBudget = 500
        val progress = ((total / maxBudget) * 100).toInt()
        val circularProgressBar = findViewById<ProgressBar>(R.id.circularProgressBar)

        circularProgressBar.progress = progress.coerceIn(0, 100)
    }


    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            result?.let {
                val spokenText = it[0]
                //Toast.makeText(this, spokenText, Toast.LENGTH_LONG).show()
                parseExpenseDetails(spokenText)
            }
        }
    }


    private fun parseExpenseDetails(spokenText: String) {

        // Regular expression to detect the amount (e.g., 500)
        val amountRegex = Regex("""\d+(\.\d{1,2})?""")
        val amount = amountRegex.find(spokenText)?.value?.toDoubleOrNull()

        // Category (You can customize categories as needed)
        val categories = listOf("groceries", "travel", "shopping", "food", "utilities","bekery","d mart","zubair bhai", "lunch","dinner","Maid","Milk", "Medicine","Sumayya","Fatima")
        val category = categories.find { spokenText.contains(it, ignoreCase = true) }

        // Description (Everything else can be treated as description)
        val description = spokenText.replace(amountRegex, "").trim()



      /*  if (amount != null ) {// Placeholder for parsing logic
            Toast.makeText(this, "Parsed: $amount", Toast.LENGTH_SHORT).show()
        }
 if (category != null ) {// Placeholder for parsing logic
            Toast.makeText(this, "Parsed: $category", Toast.LENGTH_SHORT).show()
        }*/

        if (amount != null && category != null) {
            val expense = Expense(amount = amount, category = category, description = description)
            // Save expense to database
            val viewModel = ExpenseViewModel(application)
            viewModel.insert(expense)
           // Toast.makeText(this, "Expense Saved: $amount, $category, $description", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to parse expense. Please try again.", Toast.LENGTH_SHORT).show()
        }

    }

}