# MCC-Adviser TestBed

O MCC-Adviser TestBed é um projeto que apoia a pesquisa em técnicas de **Offloading** em **Mobile Cloud Computing** (**MCC**). 
Consiste nos subprojetos **MCC-Adviser-TestBed-Server** e  **MCC-Adviser-TestBed-Client**. 

###MCC-Adviser-TestBed-Server

O projeto implementa uma aplicação servidora que simula um servidor MMC na cloud.  A comunicação cliente/servidor é realizada via socket. O servidor recebe 
o método que deve executar e retorna ao cliente o resultado dessa execução. Foi implementado na versão Neon Release (4.6.0) 
do Eclipse Java EE IDE for Web Developers . 

#####Execução
O usuário gera o JAR do projeto tendo como classe principal o arquivo MccServer. O servidor é iniciado através da 
seguinte linha de comando **java -jar mcc-server.jar -v**. O flag -v ativa o mode VERBOSE que exibe o log do servidor no console.


###MCC-Adviser-TestBed-Client

O projeto implementa um app Android que representa uma aplicação Mobile Cloud. Sua função é executar
métodos no dispositivo ou na nuvem e registrar o tempo de execução de cada chamada. Esse registro permite analisar estatisticamente
os resultados e extrair conclusões de experimentos. Foi implementado na versão 1.3 do Android Studio.

#####Execução
Na primeira execução o app cria um arquivo chamado config.xml na pasta modcs_mcc no dispositivo. Nesse arquivo é definido as informações 
de acesso ao servidor MCC e ao servidor FTP, os métodos que serão executados e o local da sua execução (DEVICE ou CLOUD), e o número
de repetições do experimento bem como o tempo de espera entre execuções.




Foi desenvolvido para dar suporte ao desenvolvimento da ferramenta MCC-Adviser(https://github.com/airtonpr/mcc_adviser).
