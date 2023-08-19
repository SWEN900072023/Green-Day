import axios from "axios";

export default axios.create({
  baseURL: "https://mes-24tf.onrender.com/mes",
  //   headers: {
  //     "Access-Control-Allow-Origin": "*", // Allow requests from any origin (not secure for production)
  //     // Add other headers as needed
  //   },
});
