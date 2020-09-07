# SIA TP2

## Instrucciones de configuración:
###### Configuraciones recomendadas
* Configuración 1:

# Cómo utilizar el JSON

```javascript
{
  "CAMPO":"VALOR",
}
```

|            CAMPO           |                                VALOR                                |                                                                                               DETALLE                                                                                               |
|:--------------------------:|:-------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|          CHARACTER         |                                ARCHER                               |                                                                                                                                                                                                     |
|                            |                                 SPY                                 |                                                                                                                                                                                                     |
|                            |                               DEFENDER                              |                                                                                                                                                                                                     |
|                            |                               WARRIOR                               |                                                                                                                                                                                                     |
|                            |                                                                     |                                                                                                                                                                                                     |
|          CROSSOVER         |                             SINGLE_POINT                            |                                                                         Se elige un gen al azar y se intercambian los alelos                                                                        |
|                            |                              TWO_POINT                              |                                                           Se eligen dos puntos para formar un rango de genes para intercambiar sus alelos                                                           |
|                            |                               ANNULAR                               |                                               Se define un punto y un rango a partir del mismo para intercambiar los alelos de los genes en ese rango                                               |
|                            |                               UNIFORM                               |                                                                         Cada gen tiene una probabilidad de ser intercambiado                                                                        |
|                            |                                                                     |                                                                                                                                                                                                     |
|          MUTATION          |                                 GENE                                |                                                                     Se altera un solo gen con probabilidad MUTATION_PROBABILITY                                                                     |
|                            |                          LIMITED_MULTIGENE                          |                                                      Se selecciona una cantidad azarosa de genes a mutar con probabilidad MUTATION_PROBABILITY                                                      |
|                            |                          UNIFORM_MULTIGENE                          |                                                                    Cada gen tiene una probabilidad MUTATION_PROBABILITY de mutar                                                                    |
|                            |                               COMPLETE                              |                                                                   Con una probabilidad MUTATION_PROBABILITY, todos los genes mutan                                                                  |
|                            |                                                                     |                                                                                                                                                                                                     |
|       IMPLEMENTATION       |                             FILL_PARENT                             | La nueva generación estará conformada por individuos seleccionados entre los hijos. En caso de necesitar más individuos para formar una población, se seleccionarán los restantes entre los padres. |
|                            |                               FILL_ALL                              |                                               La nueva generación estará conformada por individuos seleccionados entre padres e hijos sin distinciones                                              |
|                            |                                                                     |                                                                                                                                                                                                     |
| SELECTION_METHOD_{1/2/3/4} |                                ELITE                                |                                                                           Selecciona los individuos con mayor performance                                                                           |
|                            |                               ROULETTE                              |                                                          Se gira una ruleta donde los candidatos más aptos tienen mayor espacio en la misma                                                         |
|                            |                              UNIVERSAL                              |                                                                         Se gira una ruleta con resultados mejor distribuídos                                                                        |
|                            |                              BOLTZMANN                              |                                                   Se define una pseudo-aptitud dependiente de la temperatura, la cual decrece con las generaciones                                                  |
|                            |                            DET_TOURNAMENT                           |                                                        Se organizan torneos de M participantes aleatorios y el ganador de cada uno es elegido                                                       |
|                            |                           PROB_TOURNAMENT                           |                                                          Se organizan torneos de a pares y se elige aleatoriamente un ganador entre los dos                                                         |
|                            |                               RANKING                               |                                               Se define pseudo-aptitud ligada al ranking de performance del individuo y se utiliza el algoritmo ruleta                                              |
|                            |                                                                     |                                                                                                                                                                                                     |
|     PARENT_SELECTION_A     |                             [0.0 - 1.0]                             |                                       Define el porcentaje de padres seleccionados utilizando el SELECTION_METHOD_1, el resto se elige con SELECTION_METHOD_2                                       |
|                            |                                                                     |                                                                                                                                                                                                     |
|      INDIV_SELECTION_B     |                             [0.0 - 1.0]                             |                                     Define el porcentaje de individuos seleccionados utilizando el SELECTION_METHOD_3, el resto de elige con SELECTION_METHOD_4                                     |
|                            |                                                                     |                                                                                                                                                                                                     |
|            STOP            |                                 TIME                                |                                                                   Corta al pasar FACTOR milisegundos desde que inició el algoritmo                                                                  |
|                            |                             GENERATIONS                             |                                                                                  Corta al pasar FACTOR generaciones                                                                                 |
|                            |                               SOLUTION                              |                                                               Corta al encontrar un individuo con performance mayor o igual que FACTOR                                                              |
|                            |                              STRUCTURE                              |                         Corta si la tasa de reemplazo (es decir, la tasa de aparición de nuevos individuos) de las últimas FACTOR generaciones es menor que REPLACEMENT_RATE                        |
|                            |                               CONTENT                               |                                                      Corta si la mejor performance hallada permanece constante por FACTOR generaciones seguidas                                                     |
|                            |                                                                     |                                                                                                                                                                                                     |
|           FACTOR           |                                 int                                 |                                                                      Parámetro utilizado para establecer la condición de corte                                                                      |
|                            |                                                                     |                                                                                                                                                                                                     |
|         POPULATION         |                                 int                                 |                                                                            Tamaño de población al iniciar cada población                                                                            |
|                            |                                                                     |                                                                                                                                                                                                     |
|              K             |                                 int                                 |                                                                 Cantidad de individuos seleccionados cada generación para ser padres                                                                |
|                            |                                                                     |                                                                                                                                                                                                     |
|    MUTATION_PROBABILITY    |                             [0.0 - 1.0]                             |                                                                                  Probabilidad de mutación de un gen                                                                                 |
|                            |                                                                     |                                                                                                                                                                                                     |
|        TOURNAMENT_M        | [0.0 - 1.0] (solo para SELECTION_METHOD_{1/2/3/4} = DET_TOURNAMENT) |                                                                 Determina la cantidad de participantes en cada torneo determinístico                                                                |
|                            |                                                                     |                                                                                                                                                                                                     |
|      REPLACEMENT_RATE      |               [0.0 - 1.0] (solo para STOP = STRUCTURE)              |                                                                      Determina la tasa de reemplazo mínima para seguir iterando                                                                     |
|                            |                                                                     |                                                                                                                                                                                                     |

## Instrucciones de ejecución

#### Estando en el directorio raíz ejecutar los siguientes comandos:
MacOS:
```javascript
java -jar out/artifacts/TP2_jar/TP2.jar
```
Linux:
```javascript
java -jar out/artifacts/TP2_jar/TP2.jar
```
Windows:
```javascript
java -jar out/artifacts/TP2_jar/TP2.jar
```