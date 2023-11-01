import axios from "axios";

export default axios.create({
  baseURL: "https://mes-24tf.onrender.com/mes/api",
  // baseURL: "http://localhost:8090/api",
});
