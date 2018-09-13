package br.com.teste.carteiradeclientes;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.teste.carteiradeclientes.database.DadosOpenHelper;
import br.com.teste.carteiradeclientes.dominio.entidades.Cliente;
import br.com.teste.carteiradeclientes.dominio.repositorio.ClienteReporitorio;

public class Activity_Cad_Cliente extends AppCompatActivity {


    private EditText edtNome;
    private EditText edtEndereco;
    private EditText edtEmail;
    private EditText edtTelefone;
    private ConstraintLayout layoutCadCliente;

    private ClienteReporitorio clienteReporitorio;

    private SQLiteDatabase conexao;

    private DadosOpenHelper dadosOpenHelper;

    private Cliente cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cad__cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNome = (EditText)findViewById(R.id.edtNome);
        edtEndereco = (EditText)findViewById(R.id.edtEndereco);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);

        layoutCadCliente = (ConstraintLayout)findViewById(R.id.layoutCadCliente);

        criarConexao();
        verificaParametro();
    }

    private void verificaParametro(){
        Bundle bundle = getIntent().getExtras();
        cliente = new Cliente();

        if ((bundle != null) && (bundle.containsKey("CLIENTE"))){
            cliente = (Cliente) bundle.getSerializable("CLIENTE");

            edtNome.setText(cliente.nome);
            edtEndereco.setText(cliente.endereco);
            edtEmail.setText(cliente.email);
            edtTelefone.setText(cliente.telefone);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_cliente, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void confirmar(){

        if (validaCampos() == false)  {

            try {

                if (cliente.codigo == 0)
                    clienteReporitorio.inserir(cliente);
                else
                    clienteReporitorio.alterar(cliente);

                finish();

            } catch (SQLException ex){

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("Ok",null);
            dlg.show();
        }

        }
    }

    private boolean validaCampos(){
        boolean res = false;

        String nome = edtNome.getText().toString();
        String endereco = edtEndereco.getText().toString();
        String email = edtEmail.getText().toString();
        String telefone = edtTelefone.getText().toString();

        cliente.nome      = nome;
        cliente.endereco  = endereco;
        cliente.email     = email;
        cliente.telefone  = telefone;

        if (res = isCampoVazio(nome)){
            edtNome.requestFocus();
        }
        else
            if (res = isCampoVazio(endereco)){
                edtEndereco.requestFocus();
            }
            else
                if (res = !isEmailValido(email)){
                    edtEmail.requestFocus();
                }
                else
                    if (res = isCampoVazio(telefone))
                    {
                        edtTelefone.requestFocus();
                    }

        if (res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_aviso);
            dlg.setMessage(R.string.message_campo_invalidos_brancos);
            dlg.setNeutralButton("Ok",null);
            dlg.show();

        }

        return res;
    }

    private boolean isCampoVazio(String campo){
        boolean resultado = (TextUtils.isEmpty(campo) || campo.trim().isEmpty() );
        return resultado;
    }

    private boolean isEmailValido(String email){
        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return  resultado;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_ok:
                confirmar();
                break;
            case R.id.action_excluir:
                clienteReporitorio.excluir(cliente.codigo);
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void criarConexao(){

        try {

            dadosOpenHelper = new DadosOpenHelper(this);

            conexao = dadosOpenHelper.getWritableDatabase();

            Snackbar.make(layoutCadCliente, R.string.message, Snackbar.LENGTH_SHORT)
                    .setAction("Ok", null).show();

            clienteReporitorio = new ClienteReporitorio(conexao);


        } catch (SQLException ex){

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("Ok",null);
            dlg.show();
        }

    }
}
