package br.edu.ifsuldeminas.mch.tarefas2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsuldeminas.mch.tarefas2.databinding.FragmentCategoryBinding;
import br.edu.ifsuldeminas.mch.tarefas2.db.CategoryDAO;
import br.edu.ifsuldeminas.mch.tarefas2.db.TaskDAO;
import br.edu.ifsuldeminas.mch.tarefas2.domain.Category;
import br.edu.ifsuldeminas.mch.tarefas2.domain.Task;

public class CategoryFragment extends Fragment {
    private FragmentCategoryBinding binding;
    private Category category;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSaveCategoryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText nameTIET = binding.categoryDescription;

                String name = nameTIET.getText().toString();
                name = name != null ? name : "";


                if(name.equals("")){
                    Toast.makeText(getContext(), R.string.categoria_description_empty,
                            Toast.LENGTH_SHORT).show();

                } else {
                    if(category == null) {
                        Category category = new Category(0, name);

                        CategoryDAO dao = new CategoryDAO(getContext());

                       dao.save(category);

                        Toast.makeText(getContext(), R.string.category_saved,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        category.setName(name);

                        CategoryDAO dao = new CategoryDAO(getContext());
                        dao.update(category);

                        Toast.makeText(getContext(), R.string.category_updated,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

                navController.navigate(R.id.action_CategoryFragment_to_MainCategoryFragment);

                navController.popBackStack(R.id.CategoryFragment, true);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Object categoryArguments = null;
        if (getArguments() != null){
            categoryArguments = getArguments().getSerializable("category");
        }
        if (categoryArguments != null){
            category = (Category) categoryArguments;
            TextInputEditText categoryDescription = binding.categoryDescription;
            categoryDescription.setText(category.getName());
        }
    }
}