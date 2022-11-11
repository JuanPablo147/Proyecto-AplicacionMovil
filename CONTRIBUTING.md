# Contribuciones

Existen muchas formas de contribuir con [Fast Food](https://github.com/JuanPablo147/Proyecto-AplicacionMovil/). Desde sugerir una idea, hasta hacer un cambio,  mantener la información contenida actualizada, [detectar problemas](https://github.com/JuanPablo147/Proyecto-AplicacionMovil/issues/new) 
o corregirlos son formas efectivas de mejorar la comunidad.

## Informando de un problema

La forma más sencilla de contribuir con la página web es informar de un error detectado. Introduce un título corto y,
en el espacio para comentarios, indica donde detectaste el problema, y añade una pequeña descripción del mismo. 
Recuerda que puedes **añadir imágenes**, como capturas de pantalla, arrastrando la imagen sobre el editor.
## Fork y clone

Un _fork_ es su propia copia del repositorio, de esta manera podrá modificarlo y trabajar los cambios a sugerir.
Para realizar un _fork_ presione el respectivo botón en la esquina superior derecha. El proyecto copiado aparecerá en su perfil
En su copia del repositorio verá un botón `Clone or download`, si lo presiona le dará un enlace. Utilice dicho enlace para clonar su repositorio:
```
git clone https://github.com/JuanPablo147/Proyecto-AplicacionMovil.git
```
Esto creará un directorio en su computadora, llamado `ubuntu-ucr`, con todo el repositorio. Realice los cambios aquí.

## Branch

Se sugiere trabajar cada _pull request_ en una rama o _branch_ aparte. Los cambios realizados en un _branch_ no se reflejarán en la rama `master`, que es la principal.

Así se crea un nuevo _branch_:
```
git branch miaporte
```

Si ejecuta `git branch` sin más parámetros, mostrará las ramas existentes (el `*` indica la rama actual):
```
git branch
* Develop
  miaporte
```

Para cambiar a la nueva rama se ejecuta:
```
git checkout miaporte
```
Ejecutando `git branch` nuevamente podrá confirmar en cuál rama estamos:
```
git branch
  Develop
* miaporte
```
## Add y commit

Cada cambio deberíamos registrarlo en un _commit_, un _commit_ puede contener modificaciones de uno o más archivos. No incluya múltiples cambios en un solo _commit_.

Por ejemplo, si editamos el archivo `README.md`, preparamos el _commit_ así:
```
git add README.md
```
Y hacemos el _commit_ de esta manera:
```
git commit -m "Se actualizo el Readme.me"
```
donde la opción `-m "Mensaje..."` añade el mensaje para registrar el _commit_. Esto es importante para identificar qué cambio se hizo en cada _commit_.

Es posible que tengamos que hacer más de un _commit_ antes de hacer el _pull request_.

## Contribuidores ✨

[![Contributors](https://contrib.rocks/image?repo=JuanPablo147/Proyecto-AplicacionMovil )](https://github.com/JuanPablo147/Proyecto-AplicacionMovil/graphs/contributors)
