# PLATAFORMA DESENCRIPTADORA DE ALGORITMOS MQTT - WEB

Esta plataforma foi desenvolvida em springboot ainda está numa embrionária mas funcional para o caso de estudo.

## DEVELOP BY:
 * Rui Barcelos, 2190230
 * Nuno Rodrigues, 2190269
 
 Para usar esta api deverá usar os seguintes enpoints
 * Sem Autenticação
    * Endpoint /api/algorithm/\<Algoritmo>
 * Autenticação
    * Endpoint /api/algorithm/auth/\<Algoritmo>
 * Autenticação e TLS
    * Endpoint /api/algorithm/tls/\<Algoritmo>
 
|ALGORITMO       |ENDPOINT  |PASSWORD                |
|----------------|----------|------------------------|
| PLAIN TEXT     |/plain    |-                       |
| AES 128 ECB    |/aes128ecb|abcdefghijklmnop        |
| Triple DES ECB |/3desecb  |abcdefghijklmnop12345678|
| DES ECB        |/desecb   |abcdefgh                |

* Durante o trabalho foi estudado o algoritmo chacha20, mas não foi implementado nesta plataforma devido a ser 
necessária uma versão superior do java.

Requisitos:
```
JAVA 8
Maven 3
```

Compilar o projeto:
```
mvn clean install
```

Correr o projeto:
```
mvn spring-boot:run
```

Como Usar `Exemplo plain sem autenticação`:
```
Ter o brocker ligado
Arrancar este projeto
Iniciar um browser
Aceder http://localhost:8080/api/algorithm/plain
Enviar um publish
```