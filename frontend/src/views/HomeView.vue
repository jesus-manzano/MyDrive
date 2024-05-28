<template>
  <div class="home">
    <NavBar/>
    <!-- Botón inicio de sesión -->
    <router-link to="/no-existe">
      <img src="@/assets/google_sign_in.png" alt="Google Sign In">
    </router-link>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar";

export default {
  name: 'HomeView',
  components: {
    NavBar
  }
}
</script>
