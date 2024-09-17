
package audiofrec;



import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AudioFrec extends JFrame {

    private JSlider sliderAmplitud;  // Slider para controlar la amplitud (volumen)
    private JSlider sliderFrecuencia;  // Slider para controlar la frecuencia
    private JLabel etiquetaFrecuencia;  // Etiqueta para mostrar el valor actual de la frecuencia
    private JLabel etiquetaAmplitud;  // Etiqueta para mostrar el valor actual del volumen
    private JProgressBar barraTiempo;  // Barra de tiempo para mostrar la duración del sonido
    private JSpinner spinnerTiempo;  // Spinner para seleccionar el tiempo de reproducción
    private Timer timer;  // Timer para animar la barra de tiempo
    private int progreso;  // Variable de instancia para el progreso de la barra de tiempo

    // Constructor para crear la interfaz gráfica
    public AudioFrec() {
        // Configuración de la ventana
        setTitle("Generador de Sonidos con Control de Amplitud y Frecuencia");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Permite cerrar la ventana

        // Personalización de la ventana
        setUndecorated(false);  // Asegúrate de que la ventana esté decorada

        // Configurar la acción de cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (timer != null) {
                    timer.stop();  // Detener el temporizador si está en ejecución
                }
            }
        });

        setLayout(null);  // Usamos layout nulo para posicionar los componentes manualmente

        // Botón para sonido en canal izquierdo (posicionado a la izquierda)
        JButton botonIzquierdo = new JButton("Sonido Izquierdo");
        botonIzquierdo.setBounds(40, 30, 150, 40);  // Posicionamos el botón a la izquierda
        botonIzquierdo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    reproducirSonido(true);  // Sonido en canal izquierdo
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Botón para sonido en canal derecho (posicionado a la derecha)
        JButton botonDerecho = new JButton("Sonido Derecho");
        botonDerecho.setBounds(210, 30, 150, 40);  // Posicionamos el botón a la derecha
        botonDerecho.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    reproducirSonido(false);  // Sonido en canal derecho
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Slider para controlar la amplitud (volumen) de 0% a 100%
        sliderAmplitud = new JSlider(JSlider.HORIZONTAL, 0, 100, 80);  // Valor inicial del 80%
        sliderAmplitud.setBounds(50, 90, 300, 50);  // Posicionamos el slider
        sliderAmplitud.setMajorTickSpacing(20);  // Marcadores de 20 en 20
        sliderAmplitud.setPaintTicks(true);  // Mostrar las divisiones
        sliderAmplitud.setPaintLabels(true);  // Mostrar las etiquetas numéricas

        // Etiqueta para mostrar el valor del volumen en porcentaje
        etiquetaAmplitud = new JLabel("Volumen: 80%");  // Inicialmente 80%
        etiquetaAmplitud.setBounds(50, 150, 300, 30);  // Posicionamos la etiqueta

        // Agregar un ChangeListener para actualizar la etiqueta cuando se cambie el slider de amplitud
        sliderAmplitud.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int amplitud = sliderAmplitud.getValue();  // Obtener el valor del slider
                etiquetaAmplitud.setText("Volumen: " + amplitud + "%");  // Actualizar la etiqueta
            }
        });

        // Slider para controlar la frecuencia (0 Hz a 20 kHz)
        sliderFrecuencia = new JSlider(JSlider.HORIZONTAL, 0, 20000, 440);  // Frecuencia inicial de 440 Hz
        sliderFrecuencia.setBounds(50, 190, 300, 50);  // Posicionamos el slider
        sliderFrecuencia.setMajorTickSpacing(5000);  // Marcadores de 5 kHz
        sliderFrecuencia.setMinorTickSpacing(1000);  // Marcadores menores de 1 kHz
        sliderFrecuencia.setPaintTicks(true);  // Mostrar las divisiones
        sliderFrecuencia.setPaintLabels(true);  // Mostrar las etiquetas numéricas

        // Etiqueta para mostrar el valor de la frecuencia
        etiquetaFrecuencia = new JLabel("Frecuencia: 440 Hz");  // Inicialmente 440 Hz
        etiquetaFrecuencia.setBounds(50, 250, 300, 30);  // Posicionamos la etiqueta

        // Agregar un ChangeListener para actualizar la etiqueta cuando se cambie el slider de frecuencia
        sliderFrecuencia.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int frecuencia = sliderFrecuencia.getValue();  // Obtener el valor del slider
                etiquetaFrecuencia.setText("Frecuencia: " + frecuencia + " Hz");  // Actualizar la etiqueta
            }
        });

        // Spinner para seleccionar el tiempo de reproducción de 1 a 10 segundos
        spinnerTiempo = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));  // Valor inicial, mínimo, máximo, incremento
        spinnerTiempo.setBounds(50, 290, 100, 30);  // Posicionamos el spinner
        JLabel etiquetaTiempo = new JLabel("Tiempo (s):");
        etiquetaTiempo.setBounds(160, 290, 100, 30);  // Posicionamos la etiqueta del spinner

        // Barra de tiempo para mostrar la duración del sonido
        barraTiempo = new JProgressBar(0, 100);
        barraTiempo.setBounds(50, 340, 300, 30);  // Posicionamos la barra
        barraTiempo.setStringPainted(true);  // Mostrar el porcentaje en la barra
        barraTiempo.setForeground(Color.GREEN);  // Color de la barra

        // Agregar los componentes a la ventana
        add(botonIzquierdo);
        add(botonDerecho);
        add(sliderAmplitud);
        add(etiquetaAmplitud);
        add(sliderFrecuencia);
        add(etiquetaFrecuencia);
        add(spinnerTiempo);
        add(etiquetaTiempo);
        add(barraTiempo);
    }

    // Método para reproducir el sonido con amplitud y frecuencia ajustable
    private void reproducirSonido(boolean soloIzquierdo) throws LineUnavailableException {
        final int FRECUENCIA_MUESTREO = 44100;  // Calidad de CD
        final int INTERVALO_ACTUALIZACION = 50;  // Intervalo del temporizador en milisegundos

        // Obtener el tiempo de reproducción del spinner
        int duracion = (int) spinnerTiempo.getValue();  // Tiempo en segundos

        // Obtener la amplitud del slider (convertir de porcentaje a escala 0.0 a 1.0)
        double amplitud = sliderAmplitud.getValue() / 100.0;

        // Obtener la frecuencia del slider
        double frecuencia = sliderFrecuencia.getValue();

        // Buffer para almacenar datos estéreo
        byte[] buffer = new byte[FRECUENCIA_MUESTREO * duracion * 2];  // Dos canales (estéreo)

        // Generación de la onda senoidal para el canal izquierdo o derecho
        for (int i = 0; i < buffer.length / 2; i++) {
            double tiempo = i / (double) FRECUENCIA_MUESTREO;
            double angulo = 2.0 * Math.PI * frecuencia * tiempo;
            byte valorOnda = (byte) (Math.sin(angulo) * amplitud * 127);  // Escalamos por la amplitud

            if (soloIzquierdo) {
                // Canal izquierdo activo, canal derecho silencio
                buffer[2 * i] = valorOnda;
                buffer[2 * i + 1] = 0;
            } else {
                // Canal derecho activo, canal izquierdo silencio
                buffer[2 * i] = 0;
                buffer[2 * i + 1] = valorOnda;
            }
        }

        // Configuración del formato de audio
        AudioFormat formato = new AudioFormat(FRECUENCIA_MUESTREO, 8, 2, true, true);
        SourceDataLine linea = AudioSystem.getSourceDataLine(formato);
        linea.open(formato);
        linea.start();

        // Configurar la barra de tiempo y el temporizador para animar la barra
        barraTiempo.setValue(0);
        progreso = 0;
        timer = new Timer(INTERVALO_ACTUALIZACION, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                progreso += (FRECUENCIA_MUESTREO * INTERVALO_ACTUALIZACION / 1000);  // Actualiza el progreso
                barraTiempo.setValue((int) (((double) progreso / (FRECUENCIA_MUESTREO * duracion)) * 100));
                if (progreso >= (FRECUENCIA_MUESTREO * duracion)) {
                    timer.stop();
                }
            }
        });
        timer.start();

        // Reproducir el sonido en un hilo separado
        new Thread(() -> {
            linea.write(buffer, 0, buffer.length);
            linea.drain();
            linea.close();
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AudioFrec().setVisible(true));
    }
}
