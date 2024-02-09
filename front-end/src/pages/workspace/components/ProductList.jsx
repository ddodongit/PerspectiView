import { Card } from "@/components/ui/card";
import { Link } from "react-router-dom";
import WorkList from "./WorkList";

function CreateProduct() {
  return (
    <div className="flex flex-col items-center">
      <WorkList />
    </div>
  );
}
// const useTest  = () => {
//   const { data: productData, isSuccess: getProductIsSuccess } = useQuery({
//     queryKey: ["product"],
//     queryFn: async () => {
//       const response = await privateApi.post(`/api/team/3/product`,

//       );
//       console.log(response);
//       return response.data.response;
//     },
//   });

//   return { productData, getProductIsSuccess };
// };

function Product({ productImg, productName }) {
  return (
    <div className="flex flex-col items-center">
      <Card className="w-32 mx-3 my-1 h-36">
        <img className="w-full h-full rounded-xl" src={productImg} alt="cover of work" />
      </Card>
      <div className="m-2">{productName}</div>
    </div>
  );
}

export default function ProductList({ products }) {
  return (
    <div className="flex flex-wrap h-full ">
      <div className="w-full sm:w-1/2 md:w-1/3 lg:w-1/4 xl:w-1/5 ">
        <CreateProduct />
      </div>

      {products.map((product) => (
        <div className="w-full sm:w-1/2 md:w-1/3 lg:w-1/4 xl:w-1/5" key={product.productId}>
          <Link to={`/product/${product.productId}`} key={product.productId}>
            <Product productImg={product.productImageUrl} productName={product.productTitle} />
          </Link>
        </div>
      ))}
      {/* {products.map((product) => (
        <div
          className="w-full sm:w-1/2 md:w-1/3 lg:w-1/4 xl:w-1/5"
          key={product.productId}
        >
          <Link to={`/product/${product.productId}`} key={product.productId}>
            <Product
              productImg={product.productImg}
              productName={product.productName}
            />
          </Link>
        </div>
      ))} */}
      {/* FIXME 예시 이미지  */}
    </div>
  );
}
