package com.example.login2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MyAdapter myAdapter, myAdapter1;
    private RecyclerView recyclerView;
    private WordViewModel wordViewModel;
    private LiveData<List<Word>> findWord;
    private static final String WORD_VIEW = "key1";
    private static final String WORD_VIEW_KEY = "key2";
    private List<Word> allWord;
    private DividerItemDecoration dividerItemDecoration;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WordsFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFragment newInstance(String param1, String param2) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        recyclerView = requireActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        myAdapter = new MyAdapter(false, wordViewModel);
        myAdapter1 = new MyAdapter(true, wordViewModel);
        recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    int first = linearLayoutManager.findFirstVisibleItemPosition();
                    int last = linearLayoutManager.findLastVisibleItemPosition();
                    for (int i = first; i <= last; i++) {
                        MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder != null) {
                            holder.textView.setText(String.valueOf(i + 1));
                        }
                    }
                }
            }
        });
        FloatingActionButton floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });
        SharedPreferences shp = requireActivity().getSharedPreferences(WORD_VIEW, Context.MODE_PRIVATE);
        boolean ViewType = shp.getBoolean(WORD_VIEW_KEY, false);
        SharedPreferences.Editor editor = shp.edit();
        dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        if (ViewType) {
            recyclerView.setAdapter(myAdapter1);
        } else {
            recyclerView.setAdapter(myAdapter);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
        findWord = wordViewModel.getAllWordList();
        findWord.observe(getViewLifecycleOwner(), words -> {
            int temp = myAdapter.getItemCount();
            allWord = words;
            if (temp != words.size()) {
                recyclerView.smoothScrollBy(0, -200);
                myAdapter.submitList(words);
                myAdapter1.submitList(words);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Word wordDelete = allWord.get(viewHolder.getAdapterPosition());
                wordViewModel.deleteWords(wordDelete);
                Snackbar.make(requireActivity().findViewById(R.id.fragmentView), "删除了一个词汇", Snackbar.LENGTH_SHORT).
                        setAction("撤销", v -> {
                            wordViewModel.insertWords(wordDelete);
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onResume() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();
                findWord.removeObservers(getViewLifecycleOwner());
                findWord = wordViewModel.findWorMenu(pattern);
                findWord.observe(getViewLifecycleOwner(), words -> {
                    int temp = myAdapter.getItemCount();
                    allWord = words;
                    if (temp != words.size()) {
                        myAdapter.submitList(words);
                        myAdapter1.submitList(words);
                    }
                });
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                AlertDialog.Builder adg = new AlertDialog.Builder(requireActivity());
                adg.setTitle("你确定清空数据吗？");
                adg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wordViewModel.deleteAllWords();
                    }
                });
                adg.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adg.create();
                adg.show();
                break;
            case R.id.Item2:
                SharedPreferences shp = requireActivity().getSharedPreferences(WORD_VIEW, Context.MODE_PRIVATE);
                boolean ViewType = shp.getBoolean(WORD_VIEW_KEY, false);
                SharedPreferences.Editor editor = shp.edit();
                if (ViewType) {
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    editor.putBoolean(WORD_VIEW_KEY, false);
                } else {
                    recyclerView.setAdapter(myAdapter1);
                    recyclerView.removeItemDecoration(dividerItemDecoration);
                    editor.putBoolean(WORD_VIEW_KEY, true);
                }
                editor.apply();
        }
        return super.onOptionsItemSelected(item);
    }
}