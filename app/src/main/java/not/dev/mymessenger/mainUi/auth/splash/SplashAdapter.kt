package not.dev.mymessenger.mainUi.auth.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import not.dev.mymessenger.R

class SplashAdapter(
    private val items: List<SplashItem>,
) : RecyclerView.Adapter<SplashAdapter.SplashVH>() {

    class SplashVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView=itemView.findViewById<ImageView>(R.id.splash_image)
        val textView=itemView.findViewById<TextView>(R.id.splash_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplashVH {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.splash_item,parent,false)
    return SplashVH(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SplashVH, position: Int) {
        val item=items[position]
        holder.imageView.setImageResource(item.image)
        holder.textView.text=item.text
    }
}


data class SplashItem(
    val image: Int,
    val text: String,
    )