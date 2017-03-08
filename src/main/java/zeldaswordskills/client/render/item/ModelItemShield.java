/**
    Copyright (C) <2017> <coolAlias>

    This file is part of coolAlias' Zelda Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package zeldaswordskills.client.render.item;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * Shield model should be registered for front resource location only;
 * back model is retrieved from the shield's front's resource location + "_back"
 *
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ModelItemShield implements IPerspectiveAwareModel, ISmartItemModel
{
	private final IFlexibleBakedModel shieldFront;
	private List<BakedQuad> quads;

	public ModelItemShield(IBakedModel shieldFront) {
		this.shieldFront = (shieldFront instanceof IFlexibleBakedModel ? (IFlexibleBakedModel) shieldFront : new IFlexibleBakedModel.Wrapper(shieldFront, DefaultVertexFormats.ITEM));
	}

	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		ForgeHooksClient.handleCameraTransforms(shieldFront, cameraTransformType);
		return Pair.of(this, null);
	}

	@Override
	public VertexFormat getFormat() {
		return shieldFront.getFormat();
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		return this;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return shieldFront.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return shieldFront.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return shieldFront.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return shieldFront.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return shieldFront.getItemCameraTransforms();
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing face) {
		return shieldFront.getFaceQuads(face);
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		if (this.quads == null) {
			String resource = shieldFront.getParticleTexture().getIconName().replaceAll("items/", "").replaceAll("#inventory", "").replaceAll("_using", "") + "_back";
			IBakedModel shieldBack = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(resource, "inventory"));
			this.quads = Lists.newArrayList(shieldFront.getGeneralQuads());
			for (BakedQuad quad : (List<BakedQuad>) shieldBack.getGeneralQuads()) {
				if (quad.getFace() == EnumFacing.NORTH) {
					this.quads.add(quad);
					break;
				}
			}
		}
		return this.quads;
	}
}
