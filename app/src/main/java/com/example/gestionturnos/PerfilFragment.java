package com.example.gestionturnos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestionturnos.data.entities.UsuarioEntity;
import com.example.gestionturnos.data.repository.UsuarioRepository;
import com.google.android.material.button.MaterialButton;
import com.example.gestionturnos.data.repository.TurnoRepository;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    private TextView perfilNombre;
    private TextView perfilCorreo;
    private UsuarioRepository usuarioRepository;

    private TextView montoTotal;
    private TextView nroPend;
    private TextView nroConf;
    private TextView nroCan;
    private TurnoRepository turnoRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        perfilNombre = view.findViewById(R.id.perfilNombre);
        perfilCorreo = view.findViewById(R.id.perfilCorreo);

        usuarioRepository = new UsuarioRepository(requireContext());

        int userId = SessionManager.obtenerUsuarioActivo(requireContext());

        if (userId != -1) {
            UsuarioEntity usuario = usuarioRepository.obtenerUsuarioPorId(userId);

            if (usuario != null) {
                perfilNombre.setText(usuario.nombre);
                perfilCorreo.setText(usuario.correoElectronico);
            } else {
                perfilNombre.setText("Usuario no encontrado");
                perfilCorreo.setText("-");
            }
        } else {
            perfilNombre.setText("Sin sesión");
            perfilCorreo.setText("-");
        }

        montoTotal = view.findViewById(R.id.montoTotal);
        nroPend    = view.findViewById(R.id.nroPend);
        nroConf    = view.findViewById(R.id.nroConf);
        nroCan     = view.findViewById(R.id.nroCan);

        turnoRepository = new TurnoRepository(requireContext());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String yearMonthActual = sdf.format(new Date());

        if (userId != -1) {
            int pendientesMes  = turnoRepository.obtenerPendientesMes(userId, yearMonthActual);
            int confirmadosMes = turnoRepository.obtenerConfirmadosMes(userId, yearMonthActual);
            int canceladosMes  = turnoRepository.obtenerCanceladosMes(userId, yearMonthActual);
            double rendimiento = turnoRepository.obtenerRendimientoMensual(userId, yearMonthActual);

            nroPend.setText(String.valueOf(pendientesMes));
            nroConf.setText(String.valueOf(confirmadosMes));
            nroCan.setText(String.valueOf(canceladosMes));

            DecimalFormat df = new DecimalFormat("#,###");
            String textoRendimiento = df.format(Math.round(rendimiento)) + "$";
            montoTotal.setText(textoRendimiento);

        } else {
            // Si no hay usuario logueado, dejamos todo en 0
            nroPend.setText("0");
            nroConf.setText("0");
            nroCan.setText("0");
            montoTotal.setText("0$");
        }


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_perfil, container, false);
        //cerrar sesion
        ImageView btnCerrarSesion = view.findViewById(R.id.cerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> mostrarDialogCerrarSesion());
        return view;

    }
    private void mostrarDialogCerrarSesion() {
        // Crear el dialogo
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Botones del dialogo
        MaterialButton btnCerrar = dialog.findViewById(R.id.btnCerrar);
        Button btnVolver = dialog.findViewById(R.id.btnVolver);
        Button btnSalir = dialog.findViewById(R.id.btnCerrarSesion);

        // cerrar el dialogo con el boton X y el boton volver
        btnCerrar.setOnClickListener(v -> dialog.dismiss());
        btnVolver.setOnClickListener(v -> dialog.dismiss());

        // Botón Cerrar sesion, cierra el dialgo y cierra la sesion del usuario
        btnSalir.setOnClickListener(v -> {
            SessionManager.cerrarSesion(requireContext());
            dialog.dismiss();
            cerrarSesion();
        });

        dialog.show();
    }

    private void cerrarSesion() {
        // cierra sesion y envia al usuario a LogIn
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
