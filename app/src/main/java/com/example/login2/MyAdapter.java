package com.example.login2;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends ListAdapter<Word, MyAdapter.MyViewHolder> {
    boolean useCardView;
    private final WordViewModel wordViewModel;

    public MyAdapter(boolean useCardView, WordViewModel wordViewModel) {
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return (oldItem.getWord().equals(newItem.getWord())
                        && oldItem.getChineseMeaning().equals(newItem.getChineseMeaning())
                        && oldItem.isOff() == newItem.isOff());
            }
        });
        this.useCardView = useCardView;
        this.wordViewModel = wordViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itmView;
        if (useCardView) {
            itmView = layoutInflater.inflate(R.layout.cell_card, parent, false);

        } else {
            itmView = layoutInflater.inflate(R.layout.cell_normal2, parent, false);
        }
        final MyViewHolder holder = new MyViewHolder(itmView);
        holder.itemView.setOnClickListener(v -> {

            Uri uri = Uri.parse("https://fanyi.baidu.com/?aldtype=16047#en/zh/" + holder.textViewEngList.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            holder.itemView.getContext().startActivity(intent);
        });
        holder.aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Word word = (Word) holder.itemView.getTag(R.id.word_for_view);
            if (isChecked) {
                holder.textViewChinese.setVisibility(View.GONE);
                word.setOff(true);
            } else {
                holder.textViewChinese.setVisibility(View.VISIBLE);
                word.setOff(false);
            }
            wordViewModel.updateWords(word);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Word word = getItem(position);
        holder.itemView.setTag(R.id.word_for_view, word);
        holder.textView.setText(String.valueOf(position + 1));
        holder.textViewEngList.setText(word.getWord());
        holder.textViewChinese.setText(word.getChineseMeaning());
        if (word.isOff()) {
            holder.textViewChinese.setVisibility(View.GONE);
            holder.aSwitch.setChecked(true);
        } else {
            holder.textViewChinese.setVisibility(View.VISIBLE);
            holder.aSwitch.setChecked(false);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textViewEngList, textViewChinese;
        Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textViewEngList = itemView.findViewById(R.id.textViewEngList);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
            aSwitch = itemView.findViewById(R.id.switch1);

        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.textView.setText(String.valueOf(holder.getAdapterPosition() + 1));

    }
}
