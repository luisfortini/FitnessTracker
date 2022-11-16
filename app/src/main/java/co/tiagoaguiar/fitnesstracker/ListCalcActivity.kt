package co.tiagoaguiar.fitnesstracker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class ListCalcActivity : AppCompatActivity() {

    private lateinit var rvCalc: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        val type =
            intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found")

        Thread {

            val app = application as App
            val dao = app.db.calcDao()

            val response = dao.getRegisterByType(type)

            runOnUiThread {
                val adapter = MainAdapter(response)
                rvCalc = findViewById(R.id.list_calc)
                rvCalc.adapter = adapter
                rvCalc.layoutManager = LinearLayoutManager(this)
            }
        }.start()

    }

    private inner class MainAdapter(
        private val mainItems: List<Calc>
    ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MainAdapter.MainViewHolder {
            val view = layoutInflater.inflate(R.layout.list_calc_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)

        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {
                val labelTypeCalc: TextView = itemView.findViewById(R.id.label_type_list)
                val value: TextView = itemView.findViewById(R.id.value)
                val date: TextView = itemView.findViewById(R.id.date)
                val status: TextView = itemView.findViewById(R.id.status)

                labelTypeCalc.text = "${item.type.uppercase()}: "
                value.text = String.format("%.2f", item.res)

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt","BR"))
                date.text = simpleDateFormat.format(item.createdDate)
                status.text = getString(imcResponse(item.res))

            }

        }

    }

    @StringRes
    private fun imcResponse(imc: Double): Int {
        return when {
            imc < 15.0 -> R.string.imc_severely_low_weight
            imc < 16.0 -> R.string.imc_very_low_weight
            imc < 18.5 -> R.string.imc_low_weight
            imc < 25.0 -> R.string.normal
            imc < 30.0 -> R.string.imc_high_weight
            imc < 35.0 -> R.string.imc_so_high_weight
            imc < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }

    }

}