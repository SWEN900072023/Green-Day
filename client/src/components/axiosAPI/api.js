import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8090/api",
  //   headers: {
  //     "Access-Control-Allow-Origin": "*", // Allow requests from any origin (not secure for production)
  //     // Add other headers as needed
  //   },
});
