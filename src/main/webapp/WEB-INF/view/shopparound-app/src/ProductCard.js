import test_image from "./images/underwear.png";
import Card from 'react-bootstrap/Card';
import "./styles/ProductCard.css"

function CreateCard(key) {
  return (
    <Card>
      <div className="img-container">
        <Card.Img variant="top" src={test_image} />
      </div>
      <Card.Body>
        <Card.Title className="name">Card Title</Card.Title>
        <Card.Text className="description">
          Some quick example text to build on the card title and make up the
          bulk of the card's content.
        </Card.Text>
        <Card.Text className="price">
          $100
        </Card.Text>
        <Card.Text className="discount">
          50% off
        </Card.Text>
        <div className="product-available-colors">
          colors goes here
        </div>
      </Card.Body>
    </Card>
  );
}

export default function ProductCard(key) {
  return (
    <div className="card-container">
      <CreateCard key={key} />
    </div>
  );
}

  // <div key={key} className="card">
    //   <picture className="image-container">
    //     <img src={from 'react-bootstrap/Card';} alt="alternatetext" />
    //   </picture>

    //   <div className="details-container">
    //     <div className="description-container">
    //       <h3> &#60;name&#62; - description</h3>
    //     </div>

    //     <div className="price-container">
    //       <h3> $5.00 $10.99</h3>
    //     </div>

    //     <div className="discount-container">
    //       <h3>%50 off</h3>
    //     </div>

    //     <div className="color-container">
    //       <div id="red" />
    //       <div id="blue" />
    //       <div id="pink" />
    //     </div>
    //   </div>
    // </div>