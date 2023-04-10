import parse from 'html-react-parser';

//parse html string to react element
export default function htmlStringParser(prop) {
 
  const reactElement = parse(prop.htmlString);

  return reactElement;
}