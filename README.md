# OptiGest Móvil (Android, Java)

Evidencia GA8-220501096-AA2-EV02. Módulos móviles de OptiGest ERP: Login, Productos, Proveedores y Compras.

- Lenguaje: Java 17. IDE: Android Studio. minSdk 24, targetSdk 34.
- Datos: SQLite local (mismas tablas del sistema web). Contraseñas con hash SHA-256.
- Capas: modelo, datos (DAO), util (Seguridad, Permisos, Sesión) y ui (Activities).
- Usuario de demostración: admin@optigest.co (contraseña de demostración del README del sistema web).

## Generar el APK
1. Abrir la carpeta en Android Studio y esperar la sincronización de Gradle.
2. Build > Build Bundle(s) / APK(s) > Build APK(s).
3. El archivo queda en app/build/outputs/apk/debug/app-debug.apk.

## Pruebas unitarias
Ejecutar `./gradlew test` o clic derecho en app/src/test > Run Tests.
