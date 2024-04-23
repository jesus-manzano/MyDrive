<template>
  <div class="home">
    <NavBar/>
    <!-- Botón inicio de sesión -->
    <a href="/api/google-drive/signin">
      <img src="@/assets/google_sign_in.png" alt="Google Sign In">
    </a>
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
