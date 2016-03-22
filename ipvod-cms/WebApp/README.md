# GVT VOD CMS web

Módulo web de gerenciamento do conteúdo de vídeo on demand do portal de TV da GVT.

---

## Requisitos


* [Node e npm](http://nodejs.org)
* [bower](http://bower.io)
* [git](http://git-scm.com)


**GOTCHA DO NODE:** as vezes é necessário reiniciar o Windows para atualizar o `PATH` para que os comandos `node`, `npm` e `bower` fiquem disponíveis na linha de comando.

**ATENÇÃO AO INSTALAR O GIT:** em determinado passo durante a instalação, é necessário selecionar a opção que adiciona o executável do `git` ao `PATH` do Windows (não é a opção padrão do instalador).

---

## Instalação

Após instalados os requisitos basta executar os seguintes comandos:

1. `cd ~/my-projects-dir` (onde você costuma manter seus projetos)
2. `git clone https://bitbucket.org/deal-gvt/ipvod-cms`
3. `cd ipvod-cms`
4. `bower install`

Lembrando que é necessário acessar os arquivos `app/vod.html` e/ou `app/vod-public.html` utilizando um servidor de aplicação (`xmlHttpRequest` não funciona com o `file` protocol).