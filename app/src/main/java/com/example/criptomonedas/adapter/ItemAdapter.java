package com.example.criptomonedas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.criptomonedas.R;
import com.example.criptomonedas.model.Item;
import com.example.criptomonedas.ui.DetalleActivity;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private List<Item> itemList;
    private OnItemLongClickListener longClickListener;
    private OnItemClickListener clickListener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItems(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cripto, parent, false);
        return new ItemViewHolder(view, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtNombre.setText(item.getNombre());
        holder.txtSimbolo.setText(item.getSimbolo());
        holder.txtPrecio.setText("$" + item.getPrecio());

        Glide.with(context)
                .load(obtenerUrlImagen(item.getSimbolo()))
                .placeholder(R.drawable.imagen_predeterminada)
                .error(R.drawable.imagen_predeterminada)
                .into(holder.imgCripto);

        // Clic normal: Detalle
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("nombre", item.getNombre());
            context.startActivity(intent);
        });

        // Clic en el botón de editar
        holder.btnEditar.setOnClickListener(v -> {
            if (editClickListener != null) {
                editClickListener.onEditClick(position);
            }
        });

        // Clic largo: Eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            if ( onDeleteClickListener!= null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });
    }

    private String obtenerUrlImagen(String simbolo) {
        simbolo = simbolo.toLowerCase();
        return "https://cryptoicon-api.pages.dev/api/icon/" + simbolo;
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCripto;
        TextView txtNombre, txtSimbolo, txtPrecio;
        ImageButton btnEditar;
        ImageButton btnEliminar;

        public ItemViewHolder(@NonNull View itemView, OnItemLongClickListener listener) {
            super(itemView);
            imgCripto = itemView.findViewById(R.id.imgCripto);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtSimbolo = itemView.findViewById(R.id.txtSimbolo);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);


            btnEliminar.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            });
        }
    }
}
