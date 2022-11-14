package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import android.graphics.Color.GREEN
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableID = R.drawable.ic_imc_24,
                txtStringId = R.string.label_imc,
                color = R.color.lightGray
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableID = R.drawable.ic_tmb_24,
                txtStringId = R.string.label_tmb,
                color = GREEN
            )
        )
        //Criando Adapter já passando a função que é esperada para o evendo de click
        val adapter = MainAdapter(mainItems) { id ->
            when (id) {
                1 -> {
                    val intent = Intent(this, ImcActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)

    }


    // Classe do adaptador
    private inner class MainAdapter(

        //Parametros esperados
        //Lista de items
        private val mainItems: List<MainItem>,
        //Uma Função
        private val onItemClickListener: (Int) -> Unit
    ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        //Qual o layout XML do Item
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)

        }

        //Metodo que é executado quando a rolagem da tela ocorrer para trocar o conteúdo.
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)
        }

        //Quantos items a listagem vai ter
        override fun getItemCount(): Int {
            return mainItems.size
        }

        //Classe da célula (Item da lista)
        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val name: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container_imc)

                img.setImageResource(item.drawableID)
                name.setText(item.txtStringId)
                container.setBackgroundColor(item.color)

                //Declarando o evento de click na lista
                container.setOnClickListener {
                    onItemClickListener.invoke(item.id)
                }

            }

        }

    }

}