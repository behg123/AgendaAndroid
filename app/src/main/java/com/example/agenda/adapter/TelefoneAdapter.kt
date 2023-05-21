import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.model.Telefone

class TelefoneAdapter(var telefones: ArrayList<Telefone>?) : RecyclerView.Adapter<TelefoneAdapter.TelefoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelefoneViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_telefone, parent, false)
        return TelefoneViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return telefones?.size ?: 0
    }

    override fun onBindViewHolder(holder: TelefoneViewHolder, position: Int) {
        val telefone = telefones?.get(position)
        holder.bind(telefone)
    }

    inner class TelefoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val telefoneTextView: TextView = itemView.findViewById(R.id.telefoneTextView)
        private val imagemTelefone: ImageView = itemView.findViewById(R.id.fotoContato)

        fun bind(telefone: Telefone?) {
            telefone?.let {
                telefoneTextView.text = "Número: ${telefone.numero}\nTipo: ${telefone.tipo}\n"

                if (telefone.tipo == "Casa") {
                    imagemTelefone.setImageResource(R.drawable.house_icon)
                } else if (telefone.tipo == "Celular") {
                    imagemTelefone.setImageResource(R.drawable.cellphone_icon)
                } else if (telefone.tipo == "Trabalho"){
                    imagemTelefone.setImageResource(R.drawable.work_icon)
                } else {
                    imagemTelefone.setImageResource(R.drawable.noidea_icon)

                }

                telefoneTextView.text = "${telefone?.numero}\n"
            }

        }
    }
}