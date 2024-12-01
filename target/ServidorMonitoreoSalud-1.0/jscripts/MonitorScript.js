
// Define los valores de configuracion para el medidor Gauge de la temperatura
var tempData = [{
        domain: {x: [0, 1], y: [0, 1]},
        value: 33,
        title: {text: "TEMPERATURA",
            font: {
                family: "Trebuchet MS, sans-serif", // Fuente del título
                size: 16, // Tamaño de la fuente
                color: "#011c16"  // Color de la fuente
            }},
        type: "indicator",
        mode: "gauge+number",
        delta: {reference: 30,
            increasing: {color: "green"}, // Color para aumento positivo
            decreasing: {color: "red"}
        },
        gauge: {
            axis: {range: [15, 45]},
            bar: {color: "#07de6b"}
        }
    }];

// Establece el diseño de grafico para el medidor de temperaura
var templayout = {
    width: 180, height: 180,
    margin: {t: 30, b: 30, l: 30, r: 30}
};
Plotly.newPlot("temperaturaGauge", tempData, templayout);


// Define los valores de configuracion para el medidor Gauge del ritmo cardiaco
var ritmoData = [    {
        domain: {x: [0, 1], y: [0, 1]},
        value: 87,
        title: {text: "RITMO CARDIACO",
            font: {
                family: "Trebuchet MS, sans-serif", // Fuente del título
                size: 16, // Tamaño de la fuente
                color: "#011c16"  // Color de la fuente
            }},
        type: "indicator",
        mode: "gauge+number",
        delta: {reference: 90,
            increasing: {color: "green"}, // Color para aumento positivo
            decreasing: {color: "red"}
        },
        gauge: {
            axis: {range: [45, 130]},
            bar: {color: "#b51429"}
        }
    }];


// Establece el diseño de grafico para el medidor de ritmo cardiaco
var ritmolayout = {
    width: 180, height: 180,
    margin: {t: 30, b: 30, l: 30, r: 30}
};
Plotly.newPlot('ritmoGauge', ritmoData, ritmolayout);



// Define los valores de configuracion para el medidor Gauge de presion arterial
var presionData = [    {
        domain: {x: [0, 1], y: [0, 1]},
        value: 160,
        title: {text: "PRESION ARTERIAL",
            font: {
                family: "Trebuchet MS, sans-serif", // Fuente del título
                size: 16, // Tamaño de la fuente
                color: "#011c16"  // Color de la fuente
            }
        },
        type: "indicator",
        mode: "gauge+number",
        delta: {reference: 145,
            increasing: {color: "green"}, // Color para aumento positivo
            decreasing: {color: "red"}
        },
        gauge: {
            axis: {range: [120, 180]},
            bar: {color: "#eb8f1e"}
        }
    }];


// Establece el diseño de grafico para el medidor de presion arterial
var presionlayout = {
    width: 180, height: 180,
    margin: {t: 30, b: 30, l: 30, r: 30}
};
Plotly.newPlot('presionGauge', presionData, presionlayout);


// Cambia los valores marcados por del medidor de temperatura
function changeTempGaugeValue(value) {
    Plotly.update('temperaturaGauge',
            {value: [value]},
            {}
    );
}

// Cambia los valores marcados por del medidor de ritmo cardiaco
function changeRitmoGaugeValue(value) {
    Plotly.update('ritmoGauge',
            {value: [value]},
            {}
    );
}

// Cambia los valores marcados por del medidor de presion arterial
function changePresionGaugeValue(value) {
    Plotly.update('presionGauge',
            {value: [value]},
            {}
    );
}


// Define los valores de configuracion para la grafica en tiempo real de la temperatura
var datosvarietyTemp = {
    x: ["0:00:00"],
    y: [32],
    type: 'line',
    line: {color: '#07de6b', width: 5, shape: 'linear'}// Color de la línea
};

// Configuración del layout de grafica en tiempo real de la temperatura
var layoutvarietyTemp = {
    title: {
        text: "Variacion de Temperatura diaria",
        font: {
            family: "Trebuchet MS, sans-serif",
            size: 18,
            color: "black"
        }
    },
    xaxis: {title: "Hora"},
    yaxis: {title: "Temperatura"}
};

// Genera el gráfico de temperatura en el div con id "lineChart"
Plotly.newPlot('temperaturaVariabilidad', [datosvarietyTemp], layoutvarietyTemp);


// Define los valores de configuracion para la grafica en tiempo real del ritmo cardiaco
var datosRitmovariety = {
    x: ["0:00:00"],
    y: [80],
    type: 'line',
    line: {color: '#b51429', width: 5, shape: 'linear'}// Color de la línea
};

// Configuración del layout  de grafica en tiempo real de ritmo cardiaco
var layoutRitmovariety = {
    title: {
        text: "Variacion de Ritmo cardiaco diaria",
        font: {
            family: "Trebuchet MS, sans-serif",
            size: 18,
            color: "black"
        }
    },
    xaxis: {title: "Hora"},
    yaxis: {title: "Ritmo cardíaco"}
};

// Genera el gráfico de ritmo cardiaco en el div con id "lineChart"
Plotly.newPlot('ritmoVariabilidad', [datosRitmovariety], layoutRitmovariety);


// Define los valores de configuracion para la grafica en tiempo real de la presion arterial
var datosPresionvariety = {
    x: ["0:00:00"],
    y: [145],
    type: 'line',
    line: {color: '#eb8f1e', width: 5, shape: 'linear'}
};


// Configuración del layout de grafica en tiempo real de presion arterial
var layoutPresionvariety = {
    title: {
        text: "Variacion de Presion Arterial diaria",
        font: {
            family: "Trebuchet MS, sans-serif",
            size: 18,
            color: "black"
        }
    },
    xaxis: {title: "Hora"},
    yaxis: {title: "Presion arterial"}
};

// Genera el gráfico de presion arterial en el div con id "lineChart"
Plotly.newPlot('presionVariabilidad', [datosPresionvariety], layoutPresionvariety);

// Agrega un nuevo valor a la grafica en timpo real especificada por su NOMBRE
// dado un VALOR y TIEMPO establecido
function AssignPlotValue(plotName, value, time) {
    Plotly.extendTraces(plotName, {y: [[value]], x: [[time]]}, [0]);
}


// Agrega conjunto de valores a la grafica en timpo real especificada por su NOMBRE
// dado un conjunto de datos en formato JSON
function AssignPlotValuesJson(plotName, datosJson) {
    if (!Array.isArray(datosJson)) {
        console.error("El JSON debe ser un array de objetos con 'tiempo' y 'valor'.");
        return;
    }

    datosJson.forEach((item) => {
        AssignPlotValue(plotName, item.valor, item.tiempo);
    });
}


// Conexion EndPoint (WebSocket) que maneja alertas y graficos en tiempo real
var websocket = new WebSocket("ws://localhost:8080/ServidorMonitoreoSalud/wsmonitor");
websocket.onmessage = function processMessage(message) {
    try {
        var data = JSON.parse(message.data);
        if (data.tipoMensaje === 'gauges') {
            changeTempGaugeValue(data.temperatura);
            changeRitmoGaugeValue(data.ritmo);
            changePresionGaugeValue(data.presion);
            AssignPlotValue('temperaturaVariabilidad', data.temperatura, data.tiempo);
            AssignPlotValue('ritmoVariabilidad', data.ritmo, data.tiempo);
            AssignPlotValue('presionVariabilidad', data.presion, data.tiempo);
        } else if (data.tipoMensaje === 'alert'){
            agregarToast({color:data.color,texto:data.message});
        }
    } catch (error) {
        console.error("Error al procesar el mensaje JSON:", error);
    }

};

// Funcion AJAX para asignacion de valores al grafico en tiempo real variacion de temperatura
function ajaxVariacionesTemperatura(){
    http = new XMLHttpRequest();
    url = 'VariacionesServlet';
    http.open("POST", url, false);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function (){
        if (http.readyState === 4 && http.status === 200) {
            const response = JSON.parse(http.responseText);
            console.log(response);
            AssignPlotValuesJson('temperaturaVariabilidad', response);
        }
    };
    http.send('type=temperatura');
}

// Funcion AJAX para asignacion de valores al grafico en tiempo real variacion de ritmo cardiaco
function ajaxVariacionesRitmo(){
    http = new XMLHttpRequest();
    url = 'VariacionesServlet';
    http.open("POST", url, false);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function (){
        if (http.readyState === 4 && http.status === 200) {
            const response = JSON.parse(http.responseText);
            AssignPlotValuesJson('ritmoVariabilidad', response);
        }
    };    
    http.send('type=ritmo');
}

// Funcion AJAX para asignacion de valores al grafico en tiempo real variacion de presion arterial
function ajaxVariacionesPresion(){
    http = new XMLHttpRequest();
    url = 'VariacionesServlet';
    http.open("POST", url, false);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function (){
        if (http.readyState === 4 && http.status === 200) {
            const response = JSON.parse(http.responseText);
            AssignPlotValuesJson('presionVariabilidad', response);
        }
    };    
    http.send('type=presion');
}

// Funcion AJAX para asignacion de valores a los indicadores de promedios diarios de
// TEMPERATURA, RITMO CARDIACO y PRSION ARTERIAL
function ajaxPromedios(){
    http = new XMLHttpRequest();
    url = 'PromediosServlet';
    http.open("POST", url, true);
    
    http.onreadystatechange = function (){
        if (http.readyState === 4 && http.status === 200) {
            const response = JSON.parse(http.responseText);
            document.getElementById("temperatura_avg").innerText = response.temperatura;
            document.getElementById("ritmo_avg").innerText = response.ritmo;
            document.getElementById("presion_avg").innerText = response.presion;
        }
    };
    
    
    http.send();
}


/*
 *  REGION:   TOASTS (Alertas)
 */

const contenedorToast = document.getElementById('toast-container');

contenedorToast.addEventListener('click', (e) =>{
    const toastId = e.target.closest('div.toast-item').id;
    if (e.target.closest('button.btn-toast-close')) {
        cerrarToast(toastId);
    } 
});

const agregarToast = ({color, texto}) => {
    const nuevoToast = document.createElement('div');
    
    nuevoToast.classList.add('toast-item');
    nuevoToast.classList.add(color);
    
    const randomNum = Math.floor(Math.random() * 10 );
    const fecha = Date.now();
    const toastID = "toast" + fecha + randomNum;
    nuevoToast.id = toastID;
    
    const toastHtml = `
                <div class="toast-content">
                    <div class="toast-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 16 16">
                        <path  d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16m.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2"/>
                        </svg>
                    </div>
                    <div class="toast-text">
                        <p class="toast-title">Alerta</p>
                        <p class="toast-description">${texto}</p>                        
                    </div>
                </div>
                <button class="btn-toast-close" >
                    <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 16 16" class="toast-icon">
                    <path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/>
                    </svg>
                </button>
    `;
    
    nuevoToast.innerHTML = toastHtml;
    
    contenedorToast.appendChild(nuevoToast);
    
    const handleAnimationClosing = (e) => {
        if (e.animationName === 'toastClose') {
            nuevoToast.remove();
        }  
    };
    
    nuevoToast.addEventListener('animationend', handleAnimationClosing);
    
};

const cerrarToast = (id) =>{
  document.getElementById(id).classList.add('toast-closing');  
};


// Llamada a funciones AJAX para asignacion de valores generales al cargar la pagina
ajaxVariacionesTemperatura();
ajaxVariacionesRitmo();
ajaxVariacionesPresion();
ajaxPromedios();


