import Categorieslst from '../Categorieslst/Categorieslst';
import TopMenu from '../TopMenu/TopMenu';
import '../App.css';

function HomePage() {
    return (
        <div className="bg-dark min-vh-100">
            <TopMenu />
            <Categorieslst userId="6790b3ec594b4ec368666d12" />
        </div>
    );
}

export default HomePage;