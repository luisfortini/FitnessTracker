package co.tiagoaguiar.fitnesstracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import co.tiagoaguiar.fitnesstracker.model.Calc

class ImcActivity : AppCompatActivity() {

    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.search_menu){
            openListResult()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        editWeight = findViewById(R.id.edit_imc_weight)
        editHeight = findViewById(R.id.edit_imc_height)

        val btnSend: Button = findViewById(R.id.btn_imc_send)
        val btnHistory: Button = findViewById(R.id.btn_imc_history)

        btnSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.fields_messagens, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()

            val result = calculateImc(weight,height)
            //Log.d("teste", "IMC é $result")

            val textResultId = imcResponse(result)

            //Criar caixa de Diálogo
            val dialog = AlertDialog.Builder(this)

            dialog.setTitle(getString(R.string.imc_response, result))
            dialog.setMessage(textResultId)
            dialog.setPositiveButton(android.R.string.ok){dialog, which ->

            }

            dialog.setNegativeButton(R.string.save){dialog, which ->
                //Usar Thread para criar uma nova instância (execução) paralela temporária
                //para não travar a interface do app
                Thread{
                    //Gravando dados no banco de dados
                    val app = application as App
                    val dao = app.db.calcDao()
                    dao.insert(Calc(type="imc",res=result))

                    runOnUiThread {
                        openListResult()
                    }

                }.start()

            }

            val d = dialog.create()
            d.show()

            //Método de gerenciamento de teclado
            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken,0)

        }

        btnHistory.setOnClickListener {
           openListResult()
        }

    }

    private fun openListResult(){
        val intent = Intent(this@ImcActivity,ListCalcActivity::class.java)
        intent.putExtra("type", "imc")
        startActivity(intent)
    }

    @StringRes
    private fun imcResponse(imc: Double): Int{
        when{
            imc <15.0 -> return R.string.imc_severely_low_weight
            imc <16.0 -> return R.string.imc_very_low_weight
            imc <18.5 -> return R.string.imc_low_weight
            imc <25.0 -> return R.string.normal
            imc <30.0 -> return R.string.imc_high_weight
            imc <35.0 -> return R.string.imc_so_high_weight
            imc <40.0 -> return R.string.imc_severely_high_weight
            else -> return R.string.imc_extreme_weight
        }

    }

    private fun calculateImc(weight: Int, height: Int): Double{
        // peso / (altura * altura)

        return weight / ((height/100.0) * (height/100.0))
    }

    private fun validate(): Boolean {
        //Valores dos inputs não podem ser  0, nulo ou vazio

        return (editWeight.text.toString().isNotEmpty()
                && editHeight.text.toString().isNotEmpty()
                && !editWeight.text.toString().startsWith("0")
                && !editHeight.text.toString().startsWith("0"))

        //Opção mais complexa de validar os valores
        /*
        if(editWeight.text.toString().isNotEmpty()
            && editHeight.text.toString().isNotEmpty()
            && !editWeight.text.toString().startsWith("0")
            && !editHeight.text.toString().startsWith("0")){
            return true
        } else {
            return false
        }
         */
    }

}