<template>
  <div class="home">
    <!-- Sección principal con imagen de fondo -->
    <div class="hero-section d-flex align-items-center text-white">
      <div class="hero-text">
        <h1 class="display-1 fw-bold">MyDrive</h1>
        <p class="lead fs-4">El gestor de archivos en la nube definitivo</p>
        <router-link to="/filemanager/root" class="btn btn-primary btn-lg mt-4">
          Comenzar a usar
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
               class="bi bi-chevron-double-right" viewBox="0 0 16 16">
            <path fill-rule="evenodd"
                  d="M3.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L9.293 8 3.646 2.354a.5.5 0 0 1 0-.708"/>
            <path fill-rule="evenodd"
                  d="M7.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L13.293 8 7.646 2.354a.5.5 0 0 1 0-.708"/>
          </svg>
        </router-link>
      </div>
    </div>

    <!-- Sección de información superpuesta -->
    <div class="info-section container py-5">
      <div class="row text-center">
        <div class="col-md-4">
          <i class="bi bi-cloud-upload fs-1 mb-3"></i>
          <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor"
               class="bi bi-folder2-open mb-3" viewBox="0 0 16 16">
            <path
                d="M1 3.5A1.5 1.5 0 0 1 2.5 2h2.764c.958 0 1.76.56 2.311 1.184C7.985 3.648 8.48 4 9 4h4.5A1.5 1.5 0 0 1 15 5.5v.64c.57.265.94.876.856 1.546l-.64 5.124A2.5 2.5 0 0 1 12.733 15H3.266a2.5 2.5 0 0 1-2.481-2.19l-.64-5.124A1.5 1.5 0 0 1 1 6.14zM2 6h12v-.5a.5.5 0 0 0-.5-.5H9c-.964 0-1.71-.629-2.174-1.154C6.374 3.334 5.82 3 5.264 3H2.5a.5.5 0 0 0-.5.5zm-.367 1a.5.5 0 0 0-.496.562l.64 5.124A1.5 1.5 0 0 0 3.266 14h9.468a1.5 1.5 0 0 0 1.489-1.314l.64-5.124A.5.5 0 0 0 14.367 7z"/>
          </svg>
          <h3 class="mb-3">Soporte de múltiples nubes</h3>
          <p>Administra todos tus servicios en la nube desde una sola aplicación. Con MyDrive, puedes conectar todas tus
            cuentas de almacenamiento en la nube en un solo lugar, facilitando la gestión de tus archivos en diferentes
            plataformas.</p>
        </div>
        <div class="col-md-4">
          <i class="bi bi-arrows-move fs-1 mb-3"></i>
          <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor"
               class="bi bi-cloud-fog2 mb-2" viewBox="0 0 16 16">
            <path
                d="M8.5 4a4 4 0 0 0-3.8 2.745.5.5 0 1 1-.949-.313 5.002 5.002 0 0 1 9.654.595A3 3 0 0 1 13 13H.5a.5.5 0 0 1 0-1H13a2 2 0 0 0 .001-4h-.026a.5.5 0 0 1-.5-.445A4 4 0 0 0 8.5 4M0 8.5A.5.5 0 0 1 .5 8h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5"/>
          </svg>
          <h3 class="mb-3">Transferencia de archivos sin límites</h3>
          <p>Transfiere archivos entre nubes de forma rápida y segura. MyDrive te permite mover tus archivos de un
            servicio en la nube a otro sin necesidad de descargarlos a tu dispositivo, ahorrando tiempo y espacio de
            almacenamiento.</p>
        </div>
        <div class="col-md-4">
          <i class="bi bi-shield-lock fs-1 mb-3"></i>
          <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor"
               class="bi bi-file-earmark-lock mb-3" viewBox="0 0 16 16">
            <path
                d="M10 7v1.076c.54.166 1 .597 1 1.224v2.4c0 .816-.781 1.3-1.5 1.3h-3c-.719 0-1.5-.484-1.5-1.3V9.3c0-.627.46-1.058 1-1.224V7a2 2 0 1 1 4 0M7 7v1h2V7a1 1 0 0 0-2 0M6 9.3v2.4c0 .042.02.107.105.175A.64.64 0 0 0 6.5 12h3a.64.64 0 0 0 .395-.125c.085-.068.105-.133.105-.175V9.3c0-.042-.02-.107-.105-.175A.64.64 0 0 0 9.5 9h-3a.64.64 0 0 0-.395.125C6.02 9.193 6 9.258 6 9.3"/>
            <path
                d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2M9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5z"/>
          </svg>
          <h3 class="mb-3">Seguridad avanzada para tus datos</h3>
          <p>Protege tus archivos con cifrado AES. MyDrive garantiza que tus datos estén seguros
            durante todo el proceso de transferencia y almacenamiento, utilizando las tecnologías de seguridad más
            utilizadas.</p>
        </div>
      </div>
    </div>

    <!-- Secciones adicionales -->
    <section id="introduction" class="py-5 bg-light">
      <div class="container">
        <h2 class="text-center mb-5">Introducción</h2>
        <p class="lead">En el panorama actual, donde la digitalización y la movilidad están cada vez más presentes en
          nuestras vidas cotidianas y en el ámbito laboral, la gestión eficiente de los archivos y documentos se ha
          convertido en una necesidad primordial. Con la llegada de la tecnología en la nube, el almacenamiento y la
          sincronización de archivos se ha vuelto más accesible y conveniente que nunca. Sin embargo, el incremento de
          múltiples servicios de almacenamiento en la nube ha dado lugar a una fragmentación en la manera en que las
          personas gestionan sus datos, lo que puede provocar una experiencia de usuario molesta y poco eficiente.</p>
        <p>Es en este contexto surge la necesidad de una solución que unifique la gestión de almacenamiento en la nube,
          proporcionando a los usuarios una plataforma centralizada desde la cual puedan acceder, organizar y
          administrar sus archivos almacenados en diferentes servicios de almacenamiento en la nube, de manera intuitiva
          y eficaz. Así, nace "MyDrive", una aplicación web diseñada para ofrecer una gestión unificada de
          almacenamiento en la nube.</p>
      </div>
    </section>

    <section id="motivation" class="py-5">
      <div class="container">
        <h2 class="text-center mb-5">Motivación</h2>
        <p class="lead">En los últimos años, hemos sido testigos de un aumento significativo en el uso del
          almacenamiento en la nube y de herramientas en línea que no requieren instalación. Este incremento se debe, en
          gran parte, a la creciente digitalización de nuestras vidas y a la necesidad de acceder a nuestros archivos
          desde cualquier lugar y dispositivo. La conveniencia de poder almacenar, compartir y colaborar en documentos
          sin la necesidad de instalar software específico ha impulsado aún más esta tendencia, haciendo que las
          soluciones en la nube sean cada vez más populares y ampliamente adoptadas dando lugar a un sinfín de empresas
          que ofrecen servicios de almacenamiento en la nube.</p>
        <p>Aunque existen diversas soluciones de almacenamiento en la nube como Google Drive, Dropbox, OneDrive, iCloud,
          etc. Muchas de ellas presentan limitaciones en sus planes gratuitos o incluso en sus opciones de pago. Estas
          limitaciones pueden incluir restricciones en el espacio de almacenamiento, funcionalidades avanzadas solo
          disponibles en planes de pago, o dificultades para sincronizar y administrar archivos entre diferentes
          servicios de nube.</p>
      </div>
    </section>

    <!-- Nubes Disponibles -->
    <section id="cloud-services" class="py-5 bg-light">
      <div class="container">
        <h2 class="text-center mb-5">Nubes Disponibles Actualmente</h2>
        <p class="lead">MyDrive soporta actualmente las nubes más populares como Google Drive y Dropbox. Nuestro
          objetivo es continuar ampliando esta lista gracias a las contribuciones de nuestra comunidad, integrando
          nuevas nubes que se adapten a las necesidades de nuestros usuarios.</p>
        <div class="row justify-content-center my-5">
          <div class="col-md-4 text-center">
            <img src="@/assets/google-drive.png" class="img-fluid" style="max-height: 150px" alt="Google Drive">
            <p class="lead mt-1">Google Drive</p>
          </div>
          <div class="col-md-4 text-center">
            <img src="@/assets/dropbox.png" class="img-fluid" style="max-height: 150px" alt="Dropbox">
            <p class="lead mt-1">Dropbox</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Open Source Section -->
    <section id="open-source" class="py-5">
      <div class="container">
        <h2 class="text-center mb-5">Un Proyecto Open Source</h2>
        <p class="lead">MyDrive es un proyecto de código abierto, y en el futuro, cualquier colaborador será bienvenido
          para ayudar a expandir y mejorar la plataforma. Creemos en la colaboración y en la fuerza de la comunidad para
          construir una herramienta que se ajuste a las necesidades de todos. Si tienes ideas para nuevas
          características, integración de otras nubes, o cualquier otra mejora, ¡no dudes en unirte a nosotros!</p>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4">
      <div class="container text-center">
        <p>&copy; 2024 MyDrive. Todos los derechos reservados.</p>
        <p>Desarrollado por Jesús Manzano</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
}
</script>

<style scoped>
.home {
  position: relative;
}

.hero-section {
  background: url('@/assets/cloud-img.jpg') no-repeat center center;
  background-size: cover;
  height: 80vh;
  display: flex;
  align-items: center;
  padding-left: 8rem;
}

.hero-text {
  max-width: 600px;
}

.info-section {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  position: relative;
  top: -100px;
  z-index: 10;
}

.additional-info {
  background-color: #f8f9fa;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  margin-top: 20px;
}

i {
  color: #007bff;
}

h1.display-1 {
  font-size: 4rem;
}

p.lead.fs-4 {
  font-size: 1.5rem;
  color: #eaeaea;
}

h3 {
  margin-top: 15px;
  font-weight: 600;
}

p {
  color: #6c757d;
  font-size: 1rem;
}

h2.text-center {
  font-size: 2.5rem;
  font-weight: bold;
  color: #343a40;
}
</style>
