package br.com.teste.carteiradeclientes;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import br.com.teste.carteiradeclientes.database.DadosOpenHelper;
import br.com.teste.carteiradeclientes.dominio.entidades.Cliente;
import br.com.teste.carteiradeclientes.dominio.repositorio.ClienteReporitorio;

public class MainActivity extends AppCompatActivity {

    private RecyclerView lstDados;
    private FloatingActionButton fab;
    private ConstraintLayout layoutContentMain;

    private SQLiteDatabase conexao;

    private DadosOpenHelper dadosOpenHelper;
    private ClienteReporitorio clienteReporitorio;

    private ClienteAdapter clienteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lstDados = (RecyclerView) findViewById(R.id.lstDados);

        layoutContentMain = (ConstraintLayout) findViewById(R.id.LayoutContentMain);

        criarConexao();

        lstDados.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDados.setLayoutManager(linearLayoutManager);

        clienteReporitorio = new ClienteReporitorio(conexao);

        List<Cliente> dados = clienteReporitorio.getAllClientes();
        clienteAdapter = new ClienteAdapter(dados);

        lstDados.setAdapter(clienteAdapter);

    }

    private void criarConexao() {

        try {

            dadosOpenHelper = new DadosOpenHelper(this);

            conexao = dadosOpenHelper.getWritableDatabase();

            Snackbar.make(layoutContentMain, R.string.message, Snackbar.LENGTH_SHORT)
                    .setAction("Ok", null).show();


        } catch (SQLException ex) {

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();
        }

    }

    public void caadastrar(View view) {
        Intent it = new Intent(MainActivity.this, Activity_Cad_Cliente.class);
        //startActivity(it);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 0) {
            List<Cliente> dados = clienteReporitorio.getAllClientes();
            clienteAdapter = new ClienteAdapter(dados);

            lstDados.setAdapter(clienteAdapter);
        }
    }
}
