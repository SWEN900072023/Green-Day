import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import { styled } from "@mui/material/styles";
import { useEffect, useState } from "react";
import AxiosApi from "../../axiosAPI/api";
import { useSelector } from "react-redux";
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import ButtonBase from "@mui/material/ButtonBase";
import { selectorCurrentUser } from "../../store/user/user.selector";

const MyBookings = () => {
  //   const currentUser = useSelector(selectorCurrentUser);
  const currentUser = useSelector(selectorCurrentUser);
  const [bookings, setMyBookings] = useState([]);
  //   const convertDateFormat = () => {
  //     const dateObject = new Date(review.createdAt);
  //     const options = { year: "numeric", month: "short", timeZone: "UTC" };
  //     const formattedDate = dateObject.toLocaleString("en-AU", options);
  //     console.log(formattedDate);
  //     return formattedDate;
  //   };
  const cancelOrder = async (orderId) => {
    await AxiosApi.post(
      `/customer/orders/cancel/${orderId}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }
    ).then((res) => {
      console.log(res);
      alert("cancel successfully");
    });
  };
  useEffect(() => {
    async function getBookings() {
      await AxiosApi.get(`/customer/orders`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setMyBookings(res.data.data);
        // setReviews(res.data.data.reviews);
      });
    }
    getBookings();
  }, []);

  return (
    <>
      <h1>im my booking</h1>
      <div className="MyReview">
        <Box
          sx={{
            width: "80%",
            marginTop: "2rem",
          }}
        >
          {/* <Stack spacing={3} sx={{ width: "100%" }}>
          {reviews ? (
            reviews.map((review) => {
              return <Item>Item 1</Item>;
            })
          ) : (
            <h1>No review</h1>
          )}
        </Stack> */}
          {bookings.length === 0 ? (
            <h2 style={{ marginLeft: "1rem" }}>No Bookings</h2>
          ) : (
            bookings.map((booking) => {
              const start = new Date(booking.event.startTime);
              const end = new Date(booking.event.endTime);
              const options = {
                year: "numeric",
                month: "short",
                day: "numeric",
                timeZone: "UTC",
                hour: "numeric",
                minute: "numeric",
              };
              const formattedstartTime = start.toLocaleString("en-AU", options);
              // console.log(formattedDate);
              const formattedendTime = end.toLocaleString("en-AU", options);
              return (
                <Paper
                  sx={{
                    p: 2,
                    margin: "auto",
                    maxWidth: "100%",
                    flexGrow: 1,
                    backgroundColor: (theme) =>
                      theme.palette.mode === "dark" ? "#1A2027" : "#fff",
                    marginTop: "1rem",
                  }}
                >
                  <Grid container spacing={10}>
                    <Grid sx={{ width: 256, height: 256 }}></Grid>
                    <Grid item xs={30} sm container spacing={12}>
                      {/* <Grid
                      item
                      xs
                      container
                      //   mt={1}
                      direction="column"
                      //   spacing={5}
                    > */}
                      <Grid item xs width="30rem">
                        <Typography
                          gutterBottom
                          variant="subtitle1"
                          component="div"
                          fontSize="20px"
                          fontWeight="bold"
                        >
                          Event information
                        </Typography>
                        <Typography
                          variant="body2"
                          fontSize="15px"
                          gutterBottom
                        >
                          Event: {booking.event.title}
                        </Typography>
                        <Typography
                          variant="body2"
                          fontSize="15px"
                          gutterBottom
                        >
                          Artist: {booking.event.artist}
                        </Typography>
                        <Typography
                          variant="body2"
                          fontSize="15px"
                          gutterBottom
                        >
                          Duration: {formattedstartTime} - {formattedendTime}
                        </Typography>
                        {/* <Typography variant="body2" color="text.secondary">
                          ID: 1030114
                        </Typography> */}
                        <Typography
                          variant="body2"
                          fontSize="15px"
                          gutterBottom
                        >
                          Order Status: {booking.status}
                        </Typography>
                      </Grid>
                      <Grid item width="30rem">
                        <Typography
                          gutterBottom
                          variant="subtitle1"
                          component="div"
                          fontSize="20px"
                          fontWeight="bold"
                        >
                          Order information
                        </Typography>
                        {booking.subOrders.map((section) => {
                          return (
                            <>
                              <Typography
                                variant="body2"
                                fontSize="15px"
                                gutterBottom
                              >
                                Section: {section.section.name}
                              </Typography>
                              <Typography
                                variant="body2"
                                fontSize="15px"
                                gutterBottom
                              >
                                Quantity: {section.quantity}
                              </Typography>
                              <Typography
                                variant="body2"
                                fontSize="15px"
                                gutterBottom
                              >
                                Price: {section.money.unitPrice}
                              </Typography>
                            </>
                          );
                        })}
                      </Grid>

                      <Grid item>
                        <Typography
                          fontSize="30px"
                          variant="subtitle1"
                          component="div"
                        >
                          {/* ${review.tour.price} */}
                        </Typography>
                      </Grid>
                    </Grid>
                  </Grid>
                  {booking.status === "Cancelled" ? (
                    <></>
                  ) : (
                    <button onClick={() => cancelOrder(booking.id)}>
                      cancel order
                    </button>
                  )}
                </Paper>
              );
            })
          )}
        </Box>
      </div>
    </>
  );
};

export default MyBookings;
