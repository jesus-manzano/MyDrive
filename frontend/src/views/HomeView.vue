<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld :msg="msg"/>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import axios from 'axios'

export default {
  name: 'HomeView',
  components: {
    HelloWorld
  },
  data() {
    return {
      msg: '',
    };
  },
  methods: {
    fetchData() {
      // Hacer una petición GET al backend
      axios.get('/api/mensajes/hola')
          .then(response => {
            // Al recibir la respuesta, asignar los datos a responseData
            this.msg = response.data;
          })
          .catch(error => {
            // Manejar errores de la petición
            console.error('Error al obtener datos:', error);
          });
    },
  },
  mounted() {
    // Llamar al método fetchData al montar el componente
    this.fetchData();
  }
}
</script>
