package com.noberto.br.ufrn.vendapp;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.noberto.br.ufrn.vendapp.app.Mask;
import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioCliente;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.util.DatesUtil;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormClienteActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    private EditText cpNome;
    private EditText cpCpf;
    private EditText cpTelefone;
    private EditText cpEmail;
    private EditText cpNascimento;
    private RadioButton rbFeminio;
    private RadioButton rbMasculino;
    private Button btSalvarCliente;
    private Button btCancelarCliente;

    private ActionBar ab;
    private MenuItem m1;

    private DataBase dataBase;
    private SQLiteDatabase connection;
    private RepositorioCliente rpCliente;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cliente);

        conectarInterface();

        Bundle bundle = getIntent().getExtras();

        //verifica se recuperou e o parametro eh cliente
        if((bundle != null) && (bundle.containsKey(ExibirClientesActivity.PAR_CLIENTE))){

            this.cliente = (Cliente)bundle.getSerializable(ExibirClientesActivity.PAR_CLIENTE);
            ab.setSubtitle("Editar");

            preencheDados();

        }else  cliente = new Cliente();

        try {
            dataBase = new DataBase(this);
            connection = dataBase.getWritableDatabase();
            rpCliente = new RepositorioCliente(connection);
        }catch (SQLException ex){
            MensageBox.show(this, "Erro no banco: " + ex.getMessage(), "ERRO!");
        }




    }

    private void conectarInterface() {

        ab = getSupportActionBar();
        ab.setTitle("Clientes");
        ab.setSubtitle("Cadastrar");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayShowHomeEnabled(true);

        cpNome = (EditText) findViewById(R.id.cpNome);
        cpCpf = (EditText) findViewById(R.id.cpCpf);
        cpCpf.addTextChangedListener(Mask.insert("###.###.###-##", cpCpf));
        cpTelefone = (EditText) findViewById(R.id.cpTelefone);
        cpTelefone.addTextChangedListener(Mask.insert("(##)#####-####",cpTelefone));
        cpEmail = (EditText) findViewById(R.id.cpEmail);
        cpNascimento = (EditText) findViewById(R.id.cpNascimento);
        rbFeminio = (RadioButton) findViewById(R.id.rbFeminino);
        rbMasculino = (RadioButton) findViewById(R.id.rbMasculino);


        ExibeDataListener edl = new ExibeDataListener();
        cpNascimento.setOnClickListener(edl);
        cpNascimento.setKeyListener(null);
    }

    private boolean validarCampo(){
        if(TextUtils.isEmpty(cpNome.getText().toString()) ){
            MensageBox.show(this, "Erro", "O campo nome está vazio");
            return false;
        }
        if(TextUtils.isEmpty(cpCpf.getText().toString())){
            MensageBox.show(this, "Erro", "O campo cpf está vazio");
            return false;
        }
        if(TextUtils.isEmpty(cpTelefone.getText().toString())){
            MensageBox.show(this, "Erro", "O campo telefone está vazio");
            return false;
        }
        if(TextUtils.isEmpty(cpEmail.getText().toString())){
            MensageBox.show(this, "Erro", "O campo email está vazio");
            return false;
        }
        if(TextUtils.isEmpty(cpNascimento.getText().toString())){
            MensageBox.show(this, "Erro", "O campo Data de nascimento está vazio");
            return false;
        }
        if(cpCpf.getText().length() < 14){
            MensageBox.show(this, "Erro", "O campo cpf está errado");
            return false;
        }
        if(cpTelefone.getText().length() < 14){
            MensageBox.show(this, "Erro", "O campo telefone está errado");
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connection != null){
            connection.close();
        }
    }

    private void preencheDados(){
        cpNome.setText(this.cliente.getNome());
        cpCpf.setText(this.cliente.getCpf());
        cpTelefone.setText(this.cliente.getTelefone());
        cpEmail.setText(this.cliente.getEmail());
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format( cliente.getDataNascimento() );

        cpNascimento.setText(dt);

        if(cliente.getSexo().equalsIgnoreCase("Feminino")){
            rbFeminio.setChecked(true);
        }else{
            rbFeminio.setChecked(false);
            rbMasculino.setChecked(true);
        }



    }

    public void salvar() {
        if (validarCampo()){
            try {
                cliente.setNome(cpNome.getText().toString());
                cliente.setCpf(cpCpf.getText().toString());
                cliente.setTelefone(cpTelefone.getText().toString());
                cliente.setEmail(cpEmail.getText().toString());
                if (rbFeminio.isChecked()) {
                    cliente.setSexo("Feminino");
                } else {
                    cliente.setSexo("Masculino");
                }

                if (cliente.getId() == 0) {
                    rpCliente.inserir(cliente);
                } else {
                    rpCliente.alterar(cliente);
                }

                MensageBox.show(this, "Perfeito", "O cliente" + cliente.getNome() + "foi salvo!");


            } catch (Exception ex) {
                MensageBox.show(this, "Erro ao inserir cliente: " + ex.getMessage(), "ERRO!");
            }
            finish();


        }
    }

    private void exibeData(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(calendar.YEAR);
        int mes = calendar.get(calendar.MONTH);
        int dia = calendar.get(calendar.DAY_OF_MONTH);

        //usado para exibir o calendario
        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        m1 = menu.add(0, 0, 0, "Salvar");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m1.setOnMenuItemClickListener(this);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exibir_produtos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        salvar();
        return false;
    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener{

        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                exibeData();
            }
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String dataFormatada = DatesUtil.dateToString(year, monthOfYear, dayOfMonth);
            Date data = DatesUtil.getDate(year, monthOfYear, dayOfMonth);

            cpNascimento.setText(dataFormatada);
            cliente.setDataNascimento(data);
        }
    }

}
