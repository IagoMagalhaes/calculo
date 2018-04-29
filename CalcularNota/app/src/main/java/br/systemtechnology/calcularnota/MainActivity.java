package br.systemtechnology.calcularnota;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPort, edtDissertativa, edtProfessor, edtEletronica, edtRA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recuperarReferencias();

        setOnClick();
    }

    private void setOnClick() {
        findViewById(R.id.calcular).setOnClickListener(this);
    }

    private void recuperarReferencias() {
        edtPort = findViewById(R.id.edtPortfolio);
        edtDissertativa = findViewById(R.id.edtDissertativa);
        edtProfessor = findViewById(R.id.edtProfessor);
        edtEletronica = findViewById(R.id.edtEletronica);
        edtRA = findViewById(R.id.edtRA);
    }

    private int tooInt(EditText edt) throws NumberFormatException {
        return Integer.parseInt(edt.getText().toString());
    }


    private void gerarThrowIfCampoNotValido(int text) {
        if (text < 0 || text > 10) {
            throw new IllegalStateException("arg not valid");
        }
    }

    @Override
    public void onClick(View v) {
        try {
            int portfolio = tooInt(edtPort);
            int dissertativa = tooInt(edtDissertativa);
            int professor = tooInt(edtProfessor);

            int eletronica = tooInt(edtEletronica);

            valideRA();

            gerarThrowIfCampoNotValido(portfolio);
            gerarThrowIfCampoNotValido(dissertativa);
            gerarThrowIfCampoNotValido(professor);
            gerarThrowIfCampoNotValido(eletronica);


            startDialog(calcularMedia(portfolio, dissertativa, professor, eletronica));

        } catch (NumberFormatException e) {
            makeText("Algum(uns) dos campos estão inválidos!");
        } catch (IllegalStateException e) {
            makeText("Algum dos campos não estão de nota 0 a 10");
        } catch (RAException e ) {
            makeText("Verifique o RA");
        }

    }

    private void valideRA() throws RAException {
        try {
            //aqui gera o erro se nao for um inteiro,
            //so para validacao, nao precisamos de seu resultado
            tooInt(edtRA);

            if( getRA().length() != 9 ) {
                throw new RAException();
            }
        } catch (Exception e) {
            throw new RAException();
        }
    }

    private void makeText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void startDialog(float media) {
        AlertDialog.Builder alg = new AlertDialog.Builder(this);
        if (media >= 6) {
            alg.setTitle("Aluno aprovado!");
        } else {
            alg.setTitle("Aluno reprovado!");
        }
        alg.setMessage("O " + getRA() + " está com a média: " + media);
        alg.setPositiveButton("Ok" , null );
        alg.show();
    }


    private float calcularMedia(int portoflio, int dissertativa, int professor, int eletronica) {
        return ( (portoflio * 2) + (dissertativa * 3) + (professor * 2) + (eletronica * 3) ) / 10;
    }

    private String getRA() {
        return edtRA.getText().toString();
    }

    private static class RAException extends Exception {

    }
}
