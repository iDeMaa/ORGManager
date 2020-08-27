# ORGManager
# Descripción
ORGManager es un bot de Discord que te va a ayudar a organizar más al servidor de tu organización. No tendrás que preocuparte por los nombres de los usuarios ni por los rangos/roles. Además, te permite obtener información acerca de tu personaje o del personaje de algún miembro del servidor con tan sólo un comando.  
**IMPORTANTE: Actualmente sólo funciona con ORGs y usuarios de GTA V. Próximamente se ampliará para usuarios y ORGs de SAMP**  
**IMPORTANTE 2: No cambiar el nombre de los roles del Discord. De momento el bot no funciona si el nombre del rol es distinto al nombre del rango de la ORG (se le puede cambiar rol y permisos sin problemas)**  
**IMPORTANTE 3: No agregar más de un rol por usuario (temporal)**

# Discord Bot Invite Link
**\*\*ES ALTAMENTE RECOMENDABLE CREAR UN NUEVO SERVIDOR LIMPIO ANTES DE INVITAR AL BOT\*\***  
[Invitame a tu servidor!](https://discord.com/api/oauth2/authorize?client_id=746462976525205614&permissions=8&scope=bot)

# Instrucciones de uso al invitar al servidor
* El bot creará un canal privado para administradores (el cuál se llamará org-manager-temp) donde te dará las instrucciones para poder configurar el servidor.
* Tendras que obtener el ID de tu org desde el siguiente [enlace](https://unplayer.com/gtav/orgs)
* Una vez obtenido tendrás que poner en el canal que el bot creó el siguiente comando: *UP.org ID* donde **ID** es el número que obtuviste de tu ORG
  * El bot comenzará a verificar algunas cosas como la ORG, si perteneces a la misma, y si sos líder o sub-líder (Sólo éstos pueden vincular una ORG a un servidor de Discord)
* En caso de que todo esté bien, el bot creará los roles en Discord automáticamente de acuerdo a los rangos de tu ORG.
* El bot ya está listo y todo configurado.
  * A partir de ahora a cada miembro que entre al servidor de Discord se le cambiará el nickname a su nombre IC y se le asignará el rango correspondiente según su rango dentro de la ORG. 
  * En caso de que el usuario que entre no pertenezca a la ORG tendrá un rol de "Invitado"
  * En caso de que el usuario que entre no tenga su Discord vinculado al servidor oficial de UNPlayer se le cambiará el nombre indicando que no está registrado.
 
 # Instrucciones de uso para administradores
 * El bot posee un comando *UP.actualizar* que actualiará los rangos de todos los miembros del servidor (Próximamente se agregará para que se ejecute automáticamente cada cierto tiempo)

# Instrucciones de uso para usuarios
* El bot posee un comando *UP.yo* el cuál traera toda la información del usuario que ejecutó el comando. Esta información incluye:
  * Nombre IC (el cuál si le haces click te llevará al perfil del foro)
  * Nivel
  * Tiempo jugado
  * Certificación
  * Estado de banneo
  * Listado de organizaciones y el respectivo rango
* Así mismo el bot tiene el comando *UP.usuario @Usuario* donde @Usuario es el tag de algún usuario que sea miembro del servidor y trae la información IC del mismo de la misma forma que el comando *UP.yo*. Ej: *UP.usuario @Thomas_Lawrence*

# Características
- [x] Vinculación de servidor Discord con ORG
- [x] Creación de roles basados en los rangos de la ORG
- [x] Asignación de nickname según el nombre IC del usuario
- [x] Asignación del rol según el rango IC del usuario
- [x] Funcionalidad con el servidor GTA V
- [x] Actualización de los rangos de los usuarios
- [x] Muestra de información IC del usuario
- [x] Muestra de información IC de otro usuario
- [ ] Funcionalidad con el servidor SAMP
- [ ] Actualización automática de los rangos periódicamente
- [ ] Actualización de la ORG (por si se añade, quita o modifica algún rango)

# Sugerencias
Cualquier sugerencia o crítica constructiva es bienvenida. Comunicate con DeMaa#1038 en Discord en caso de que tengas una!
